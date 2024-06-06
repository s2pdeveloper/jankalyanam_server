package com.app.service.imp;

import java.lang.reflect.Field;
import java.util.* ;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.app.constant.ServiceConstant;
import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.PROVIDED;
import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DistrictDTO;
import com.app.dto.DonorDTO;
import com.app.dto.MonthlyCountDTO;
import com.app.dto.NotificationRequest;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.StateDTO;
import com.app.dto.TahsilDTO;
import com.app.dto.TahsilRequestDTO;
import com.app.dto.TahsilResponseDTO;
import com.app.dto.UserDTO;
import com.app.dto.VillageDTO;
import com.app.dto.VillageRequestDTO;
import com.app.dto.VillageResponseDTO;
import com.app.exception.InvalidInputException;
import com.app.model.BloodRequestDO;
import com.app.model.DistrictDO;
import com.app.model.DonorDO;
import com.app.model.StateDO;
import com.app.model.TahsilDO;
import com.app.model.UserDO;
import com.app.model.VillageDO;
import com.app.repository.BloodRequestRepository;
import com.app.repository.DistrictRepository;
import com.app.repository.DonorRepository;
import com.app.repository.StateRepository;
import com.app.repository.TahsilRepository;
import com.app.repository.VillageRepository;
import com.app.service.BloodRequestService;
import com.app.service.DonorService;
import com.app.service.HelperService;
import com.app.service.UserDeviceIdService;
import com.app.utilities.Utility;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;



@Service
@Slf4j
public class HelperServiceImp implements HelperService{
   
	@Autowired
    private BloodRequestRepository bloodRequestRepository;
	
	@Autowired
    private DonorRepository donorRepository;
	
	@Autowired
    private StateRepository stateRepository;
	
	@Autowired
    private DistrictRepository districtRepositoy;
	
	@Autowired
    private TahsilRepository tahsilRepositoy;
	
	@Autowired
    private VillageRepository villageRepositoy;
	
	@Autowired
	@Qualifier("cachedThreadPool")
	private ExecutorService executorService;
	
	@Autowired
	private UserDeviceIdService userDeviceIdService;
	
	@Autowired
	private FCMService fcmService;

	@Override
	public ResultDTO assignOrRemoveToBloodRequest(Long id,Boolean allocate) {
		Optional<DonorDO> donorDetails = donorRepository.findById(id);
		DonorDO data = donorDetails.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
	
		if(allocate) {
			data.setStatus(DONOR_STATUS.ALLOCATED);
		}else {
			data.setStatus(DONOR_STATUS.PENDING);
		}
		
		data.setLocation(null);
		donorRepository.save(data);
		executorService.execute(() -> {
			String title = "Donation";
			String body = null;
			String deviceId = null;
			if(allocate) {
				  DonorDO donor = donorRepository.findById(id).orElse(null);
				  body =String.format("A Beneficier %s has been Allocated.Location %s",
						  donor.getBloodRequest().getName(),
	                     donor.getBloodRequest().getHospitalName());
				  deviceId = userDeviceIdService.getDeviceId(donor.getUserId());	  
			
			}
			else  {
				  DonorDO donor = donorRepository.findById(id).orElse(null);
				  body =String.format("Thanks for your support %s But the beneficier got another Donor.You can donate blood to the specified blood bank",
	                     donor.getName());
				  deviceId = userDeviceIdService.getDeviceId(donor.getUserId());	  
			
			}

			if(StringUtils.hasText(deviceId)) {
				NotificationRequest notify = new NotificationRequest(title,body,Arrays.asList(deviceId));
				try {
					fcmService.sendMessageToToken(notify);
				} catch (FirebaseMessagingException | InterruptedException | ExecutionException e) {
					log.info("--------ERROR IN FIREBASE---------");
					e.printStackTrace();
					
				}
			}
			
		});
		
		return new ResultDTO(id.toString(),"Updated Successfully!");
	}

	@Override
	public void acceptOrCancelByDonor(Long id,Boolean add) {
		Optional<DonorDO> donorDetails = donorRepository.findById(id);
		DonorDO data = donorDetails.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		if(donorDetails.get().getBloodRequest() != null && add) {
			Optional<BloodRequestDO> bloodRequestData = bloodRequestRepository.findById(donorDetails.get().getBloodRequest().getId());
			BloodRequestDO b = bloodRequestData.orElse(null);
			if(b != null) {
				b.setStatus(BLOOD_STATUS.ALLOCATED);
				bloodRequestRepository.save(b);
				data.setDonationDate(b.getBloodRequireDate());
				donorRepository.save(data);
			}
		
			
		}
		else if(donorDetails.get().getBloodRequest() != null && !add) {
			Optional<BloodRequestDO> bloodRequestData = bloodRequestRepository.findById(donorDetails.get().getBloodRequest().getId());
			BloodRequestDO b = bloodRequestData.orElse(null);
			if(b != null) {
			b.setStatus(BLOOD_STATUS.ACCEPTED);		
			b.setDonorId(null);		
			b.setProvided(null);
			bloodRequestRepository.save(b);
			}
		}
		
	}
	
	@Override
	public void CancelBloodRequest(Long id) {
		Optional<BloodRequestDO> bloodRequestData = bloodRequestRepository.findById(id);
		BloodRequestDO data = bloodRequestData.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		if(data.getDonorId() != null) {
			Optional<DonorDO> donorDetails = donorRepository.findById(data.getDonorId());
			DonorDO donarDetails = donorDetails.orElse(null);
			if(donarDetails != null) {
				donarDetails.setStatus(DONOR_STATUS.PENDING);
				donorRepository.save(donarDetails);
			}
			data.setDonorId(null);	
			data.setProvided(null);
			bloodRequestRepository.save(data);
		}
		
		
	}

	@Override
	public ResultDTO bulkInsertState(List<StateDO> states) {
		stateRepository.saveAll(states);
		return new ResultDTO("","States Inserted Successfully!");
	}

	@Override
	public ResultDTO bulkInsertDistrict(List<DistrictDO> district) {
		districtRepositoy.saveAll(district);
		return new ResultDTO("","District Inserted Successfully!");
	}

	@Override
	public ResultDTO bulkInsertTahsil(List<TahsilDO> tahsil) {
		tahsilRepositoy.saveAll(tahsil);
		return new ResultDTO("","Tahsil Inserted Successfully!");
	}

	@Override
	public ResultDTO bulkInsertVillage(List<VillageDO> village) {
		villageRepositoy.saveAll(village);
		return new ResultDTO("","Village Inserted Successfully!");
	}
	
	@Override
	public List<StateDO> getAll() {
		return stateRepository.findAll();
	}

	@Override
	public List<StateDTO> getAllState() {
		List<StateDO> states = stateRepository.findAll();
		List<StateDTO> statelist = Utility.mapList(states, StateDTO.class);
		return statelist;
	}

	@Override
	public List<DistrictDTO> getDistrictByStateId(Long stateId) {
		List<DistrictDO> districts = districtRepositoy.findByStateId(stateId);
		List<DistrictDTO> districtlist = Utility.mapList(districts, DistrictDTO.class);
		return districtlist;
	}

	@Override
	public List<TahsilDTO> getTahsilByDistrictId(Long districtId) {
		List<TahsilDO> tahsils = tahsilRepositoy.findByDistrictId(districtId);
		List<TahsilDTO> tahsillist = Utility.mapList(tahsils, TahsilDTO.class);
		return tahsillist;
	}

	@Override
	public List<VillageDTO> getVillageByTahsilId(Long tahsilId) {
		List<VillageDO> villages = villageRepositoy.findByTahsilId(tahsilId);
		List<VillageDTO> villageList = Utility.mapList(villages, VillageDTO.class);
		return villageList;
	}

	@Override
	public ResultDTO addTahsil(TahsilRequestDTO tahsil) {
		TahsilDO mapTahsilRequest = Utility.mapObject(tahsil, TahsilDO.class);
		tahsilRepositoy.save(mapTahsilRequest);
		return new ResultDTO("","Successfully Created!");
	}

	@Override
	public ResultDTO updateTahsil(Long id, TahsilRequestDTO tahsil) {
		Optional<TahsilDO> tahsilData = tahsilRepositoy.findById(id);
		TahsilDO data = tahsilData.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		data.setTahsilName(tahsil.getTahsilName());
		data.setDistrictId(tahsil.getDistrictId());
		tahsilRepositoy.save(data);
		return new ResultDTO("","Successfully Updated!");
	}

	@Override
	public ResultDTO deleteTahsil(Long id) {
		tahsilRepositoy.deleteById(id);
		return new ResultDTO("","Successfully Deleted!");
	}

	@Override
	public TahsilDTO getTahsilById(Long id) {
		Optional<TahsilDO> tahsilData = tahsilRepositoy.findById(id);
		TahsilDO data = tahsilData.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		
		TahsilDTO tahsil = Utility.mapObject(data, TahsilDTO.class);
		return tahsil;
	}

	@Override
	public ResultDTO addVillage(VillageRequestDTO village) {
		VillageDO mapRequest = Utility.mapObject(village, VillageDO.class);
		villageRepositoy.save(mapRequest);
		return new ResultDTO("","Successfully Created!");
	}

	@Override
	public ResultDTO updateVillage(Long id, VillageRequestDTO village) {
		Optional<VillageDO> villageData = villageRepositoy.findById(id);
		VillageDO data = villageData.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		data.setVillageName(village.getVillageName());
		data.setTahsilId(village.getTahsilId());		
		villageRepositoy.save(data);
		return new ResultDTO("","Successfully Updated!");
	}

	@Override
	public ResultDTO deleteVillage(Long id) {
		villageRepositoy.deleteById(id);
		return new ResultDTO("","Successfully Deleted!");
	}

	@Override
	public VillageDTO getVillageById(Long id) {
		Optional<VillageDO> villageData = villageRepositoy.findById(id);
		VillageDO data = villageData.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		VillageDTO village = Utility.mapObject(data, VillageDTO.class);
		return village;
	}

	@Override
	public ResponseDTO<TahsilResponseDTO> getAll(Integer pageNo, Integer pageSize, String sortBy, String districtId, String search) {
		Page<TahsilDO> tahsilList;
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		Specification<TahsilDO> specification = (root, query, cb) -> {
			 List<Predicate> predicates = new ArrayList<>();
			 	if (districtId != null && !districtId.isEmpty()) {
		            predicates.add(cb.equal(root.get("districtId"), districtId));
		        }
		        if (search != null && !search.isEmpty()) {
		            String searchString = "%" + search.toLowerCase() + "%";
		            List<Predicate> attributePredicates = new ArrayList<>();
		            for (SingularAttribute<? super TahsilDO, ?> attribute : root.getModel().getSingularAttributes()) {
		                if (attribute.getJavaType() == String.class) {
		                    Expression<?> attributePath = root.get(attribute);
		                    if (attributePath.getJavaType() == String.class) {
		                        attributePredicates.add(cb.like(cb.lower((Expression<String>) attributePath), searchString));
		                    }
		                }
		            }
		            if (!attributePredicates.isEmpty()) {
		                predicates.add(cb.or(attributePredicates.toArray(new Predicate[0])));
		            }
		        }
		        Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
		        return finalPredicate;
		 };
		 
		 	tahsilList = tahsilRepositoy.findAll(specification, paging);
		    List<TahsilResponseDTO> tahsilDTOList = Utility.mapList(tahsilList.getContent(), TahsilResponseDTO.class);
		    return new ResponseDTO<TahsilResponseDTO>(tahsilList.getTotalElements(), tahsilList.getTotalPages(), tahsilDTOList);
	}

	@Override
	public ResponseDTO<VillageResponseDTO> getAllVillage(Integer pageNo, Integer pageSize, String sortBy,
			String tahsilId, String search) {
		Page<VillageDO> villageList;
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		Specification<VillageDO> specification = (root, query, cb) -> {
			 List<Predicate> predicates = new ArrayList<>();
			 	if (tahsilId != null && !tahsilId.isEmpty()) {
		            predicates.add(cb.equal(root.get("tahsilId"), tahsilId));
		        }
		        if (search != null && !search.isEmpty()) {
		            String searchString = "%" + search.toLowerCase() + "%";
		            List<Predicate> attributePredicates = new ArrayList<>();
		            for (SingularAttribute<? super VillageDO, ?> attribute : root.getModel().getSingularAttributes()) {
		                if (attribute.getJavaType() == String.class) {
		                    Expression<?> attributePath = root.get(attribute);
		                    if (attributePath.getJavaType() == String.class) {
		                        attributePredicates.add(cb.like(cb.lower((Expression<String>) attributePath), searchString));
		                    }
		                }
		            }
		            if (!attributePredicates.isEmpty()) {
		                predicates.add(cb.or(attributePredicates.toArray(new Predicate[0])));
		            }
		        }
		        Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
		        return finalPredicate;
		 };
		 
		 	villageList = villageRepositoy.findAll(specification, paging);
		    List<VillageResponseDTO> villageDTOList = Utility.mapList(villageList.getContent(), VillageResponseDTO.class);
		    return new ResponseDTO<VillageResponseDTO>(villageList.getTotalElements(), villageList.getTotalPages(), villageDTOList);
	}
}
