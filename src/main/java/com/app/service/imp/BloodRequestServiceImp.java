package com.app.service.imp;

import java.lang.reflect.Field;
import java.util.* ;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import javax.persistence.EntityManager;
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
import com.app.constant.ServiceConstant.PROVIDED;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DonorDTO;
import com.app.dto.NotificationRequest;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.exception.InvalidInputException;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.model.UserDO;
import com.app.repository.BloodRequestRepository;
import com.app.service.BloodRequestService;
import com.app.service.DonorService;
import com.app.service.UserDeviceIdService;
import com.app.utilities.Utility;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;



@Service
@Slf4j
public class BloodRequestServiceImp implements BloodRequestService{
   
	@Autowired
    private BloodRequestRepository bloodRequestRepository;

	@Autowired
	private FCMService fcmService;
	
	@Autowired
	private DonorService donorService;
	
	@Autowired
	@Qualifier("cachedThreadPool")
	private ExecutorService executorService;
	
	@Autowired
	private UserDeviceIdService userDeviceIdService;
	
	@Override
	public BloodRequestDTO getById(Long id) {
		Optional<BloodRequestDO> data = bloodRequestRepository.findById(id);
		BloodRequestDO b = data.orElse(null);
		BloodRequestDTO result = Utility.mapObject(b,BloodRequestDTO.class);
		return result;
		
	}
	
	@Override
	public ResultDTO createRequest(BloodDTO bloodRequest) {
		BloodRequestDO mapBloodRequest = Utility.mapObject(bloodRequest, BloodRequestDO.class);
		System.out.println("bloodRequest---"+bloodRequest.toString());
		if(bloodRequest.getIsWebsite() == null || !bloodRequest.getIsWebsite()) {
			mapBloodRequest.setAttenderId(Utility.getSessionUser().getId());
		}
		System.out.println("mapBloodRequest---"+mapBloodRequest.toString());
	    bloodRequestRepository.save(mapBloodRequest);
		executorService.execute(() -> {
			List<String> deviceIds = userDeviceIdService.getAdminsAndDeviceId();
			if(deviceIds.size() > 0) {
				String title = "Blood Request";
				String body = String.format("New Request for %s Blood Group By %s Location: %s",
                        bloodRequest.getBloodGroup(),
                        bloodRequest.getName(),
                        bloodRequest.getLocation());
				NotificationRequest notify = new NotificationRequest(title,body,deviceIds);
				try {
					fcmService.sendMessageToToken(notify);
				} catch (FirebaseMessagingException | InterruptedException | ExecutionException e) {
					log.info("--------ERROR IN FIREBASE---------");
					e.printStackTrace();
					
				}
			}
			
		});
		return new ResultDTO("","Successfully Created!");
	  
	}
    
	@Override
    public ResponseDTO<BloodRequestDTO> getByStatus(String type,Integer pageNo, Integer pageSize, String sortBy, String search) { 
	
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
		Page<BloodRequestDO> BloodRequestList;
			if(type.equals("HISTORY")) {
				 BloodRequestList = bloodRequestRepository.findAllByStatus(List.of(BLOOD_STATUS.DONE),search,paging);
				 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
				 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
			}else if(type.equals("ACTIVE")) {
				 BloodRequestList =bloodRequestRepository.findAllByStatus(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.ALLOCATED,BLOOD_STATUS.RECEIVED),search,paging);
				 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
				 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
			}else if(type.equals("MYLIST")) {
				
				BloodRequestList = bloodRequestRepository.findByStatusAndAdminId(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.ALLOCATED,BLOOD_STATUS.RECEIVED),Utility.getSessionUser().getId(),search, paging);
				List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
				 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
			
			}else {
				throw new InvalidInputException("Invalid Input");
			}
	
	  
    }



	@Override
	public ResponseDTO<BloodRequestDTO> getAllRequest(Integer pageNo, Integer pageSize, String sortBy,String status,String startDate,String endDate ,String search) {	
		
	    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

	    Page<BloodRequestDO> pageResult = bloodRequestRepository.findAll((root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (status != null && !status.isEmpty()) {
	            predicates.add(cb.equal(root.get("status"), ServiceConstant.BLOOD_STATUS.valueOf(status)));
	        }

	        if (startDate != null && !startDate.isEmpty()) {
	            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), Utility.parseDate(startDate)));
	        }

	        if (endDate != null && !endDate.isEmpty()) {
	            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), Utility.parseDate(endDate)));
	        }

	        if (search != null && !search.isEmpty()) {
	            String searchString = "%" + search.toLowerCase() + "%";
	            List<Predicate> attributePredicates = new ArrayList<>();
	            for (SingularAttribute<? super BloodRequestDO, ?> attribute : root.getModel().getSingularAttributes()) {
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
	    }, pageable);

	    List<BloodRequestDTO> BloodRequestlist = Utility.mapList(pageResult.getContent(), BloodRequestDTO.class);
	    return new ResponseDTO<BloodRequestDTO>(pageResult.getTotalElements(), pageResult.getTotalPages(),BloodRequestlist);

	}

	@Override
	public ResponseDTO<BloodRequestDTO> getByStatusAndAttenderId(String type,Integer pageNo, Integer pageSize, String sortBy, String search) {
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
		Page<BloodRequestDO> BloodRequestList;
		if(type.equals("HISTORY")) {
			 BloodRequestList = bloodRequestRepository.findByStatusInAndAttenderId(List.of(BLOOD_STATUS.DONE),Utility.getSessionUser().getId(),search, paging);
			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
			 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
		}else if(type.equals("ACTIVE")) {
			 BloodRequestList =bloodRequestRepository.findByStatusInAndAttenderId(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.ALLOCATED,BLOOD_STATUS.RECEIVED),Utility.getSessionUser().getId(),search, paging);
			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
			 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
		}
		else {
			throw new InvalidInputException("Invalid Input");
		}
		
	}

	@Override
	public ResultDTO acceptRequest(Long id,BLOOD_STATUS status) {
		BLOOD_STATUS changeStatus = null;
	
		switch(status) {
		  case ACCEPTED:
			  changeStatus = BLOOD_STATUS.ACCEPTED;
			  bloodRequestRepository.findAndChangeAdmin(id,changeStatus,Utility.getSessionUser().getId());
		    break;
		  case PENDING:
			  changeStatus = BLOOD_STATUS.PENDING;
			  bloodRequestRepository.findAndChangeAdmin(id,changeStatus,null);
		    break;
		  case RECEIVED:
			  changeStatus = BLOOD_STATUS.RECEIVED;
			  bloodRequestRepository.findByIdAndUpdateStatus(id,changeStatus);
			  break;
		  case DONE:
			  changeStatus = BLOOD_STATUS.DONE;
			  bloodRequestRepository.findByIdAndUpdateStatus(id,changeStatus);
			  break;
		  default:
			  throw new InvalidInputException("Invalid Input");
		     
		}
		
	
		executorService.execute(() -> {
			String title = "Blood Request";
			String body = null;
			String deviceId = null;
			if(status.equals(BLOOD_STATUS.ACCEPTED)) {
				  BloodRequestDO bloodRequest = bloodRequestRepository.findById(id).orElse(null);
				  body =String.format("Request By %s is Accepted By %s %s",
	                      bloodRequest.getName(),
	                      Utility.getSessionUser().getFirstName(),
	                      Utility.getSessionUser().getLastName());
				  deviceId = userDeviceIdService.getDeviceId(bloodRequest.getAttenderId());	  
			
			}
			else if(status.equals(BLOOD_STATUS.RECEIVED)) {
				  BloodRequestDO bloodRequest = bloodRequestRepository.findById(id).orElse(null);
				  body =String.format("%s Received Blood",
	                      bloodRequest.getName());
				  deviceId = userDeviceIdService.getDeviceId(bloodRequest.getAcceptorId());	  
			
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
		
		return new ResultDTO(id.toString(),"Blood Request Accepted Successfully!");
	}

	@Override
	public ResultDTO updateById(Long id, BloodRequestUpdateDTO updateData) {
		Optional<BloodRequestDO> bloodRequest = bloodRequestRepository.findById(id);
		BloodRequestDO data = bloodRequest.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		 String deviceId = userDeviceIdService.getDeviceId(data.getAttenderId());
	
		if(updateData.getDonorId() == null && updateData.getBloodBankName() == null) {
			throw new InvalidInputException("Please Fill Atleast One Information");
		}
		if(data.getDonorId() != null) {
			executorService.execute(() -> {
				donorService.assignOrRemoveToBloodRequest(data.getDonorId(),false);
			});
			
		}
		if(updateData.getProvided().equals(PROVIDED.DONOR) && updateData.getDonorId() != null) {
		
			donorService.assignOrRemoveToBloodRequest(updateData.getDonorId(),true);
			data.setDonorId(updateData.getDonorId());
			data.setBloodBankName(null);
			data.setBankState(null);
			data.setBankCity(null);
			
		}else {
			data.setBloodBankName(updateData.getBloodBankName());
			data.setBankState(updateData.getBankState());
			data.setBankCity(updateData.getBankCity());
			data.setDonorId(null);		
	
		}
		data.setProvided(updateData.getProvided());
		data.setStatus(BLOOD_STATUS.ALLOCATED);
		bloodRequestRepository.save(data);

		
		executorService.execute(() -> {
			String title = "Blood Request";
			 String body = null;
			if(StringUtils.hasText(deviceId)) {
				if(updateData.getProvided().equals(PROVIDED.DONOR)) {
					body =String.format("Donor Allocated for Request by %s %s",
			                 Utility.getSessionUser().getFirstName(),
			                 Utility.getSessionUser().getLastName());
				}else {
					body =String.format("Blood from Blood Bank %s Allocated for Request by %s %s",
							updateData.getBloodBankName(),
							Utility.getSessionUser().getFirstName(),
			                Utility.getSessionUser().getLastName());
				}
			
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


	

    


}
