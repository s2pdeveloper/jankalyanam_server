package com.app.service.imp;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.app.constant.ServiceConstant;
import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.BloodRequestDTO;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.NotificationRequest;
import com.app.dto.ProfileResponseDTO;
import com.app.dto.ProfileUploadDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.exception.InvalidInputException;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.model.UserDO;
import com.app.repository.BloodRequestRepository;
import com.app.repository.DonorRepository;
import com.app.service.CloudinaryService;
import com.app.service.DonorService;
import com.app.service.HelperService;
import com.app.service.UserDeviceIdService;
import com.app.utilities.Utility;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DonorServiceImp implements DonorService{
   
	@Autowired
    private DonorRepository donorRepository;
	
	@Autowired
	private FCMService fcmService;
	
	@Autowired
	private HelperService helperService;
	
	@Autowired
	@Qualifier("cachedThreadPool")
	private ExecutorService executorService;
	
	@Autowired
	private UserDeviceIdService userDeviceIdService;
	
	@Autowired CloudinaryService cloudinaryService;

	@Value("${cloudinary.url}")
	private String filePath;
	
	@Override
	public DonorDTO getDonorDetails(Long id) {
		Optional<DonorDO> donorDetails = donorRepository.findById(id);
		DonorDO data = donorDetails.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		return Utility.mapObject(data, DonorDTO.class);	
		
	}

	@Override
	public DonorDTO createRequest(DonorRequestDTO donorRequestDTO) {
		DonorDO donor = Utility.mapObject(donorRequestDTO, DonorDO.class);
		if(donorRequestDTO.getIsWebsite() == null || !donorRequestDTO.getIsWebsite()) {
			donor.setUserId(Utility.getSessionUser().getId());
		}
	
        donor.setBloodRequest(null);
		DonorDO donorSave = donorRepository.save(donor);
	
		executorService.execute(() -> {
			List<String> deviceIds = userDeviceIdService.getAdminsAndDeviceId();
			
			if(deviceIds.size() > 0) {
				String title = "Donation";
				String body = String.format("New Request for %s Donation By %s Location: %s",
						donorRequestDTO.getBloodGroup(),
						donorRequestDTO.getName(),
						donorRequestDTO.getState());
				NotificationRequest notify = new NotificationRequest(title,body,deviceIds);
				try {
					fcmService.sendMessageToToken(notify);
				} catch (FirebaseMessagingException | InterruptedException | ExecutionException e) {
					log.info("--------ERROR IN FIREBASE---------");
					e.printStackTrace();
					
				}
			}
			
		});
		return Utility.mapObject(donorSave, DonorDTO.class);
	 
	}

	@Override
	public ResponseDTO<DonorDTO> getByStatus(String type, Integer pageNo, Integer pageSize, String sortBy, String search,String bloodBankName,List<String> bloodGroup,String donationDate) {
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
			
		Page<DonorDO> donorList;
//		if(type.equals("HISTORY")) {
//				donorList = donorRepository.findByStatusIn(List.of(DONOR_STATUS.CLOSE,DONOR_STATUS.CANCEL),search,paging);
//				 List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
//				 return new ResponseDTO<DonorDTO>(donorList.getTotalElements(),donorList.getTotalPages(),donorListDTO);
//			}else if(type.equals("ACTIVE")) {
//				 donorList =donorRepository.findByStatusIn(List.of(DONOR_STATUS.ALLOCATED,DONOR_STATUS.PENDING,DONOR_STATUS.ACCEPTED,DONOR_STATUS.DONE),search,paging);
//				 List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
//				 return   new ResponseDTO<DonorDTO>(donorList.getTotalElements(),donorList.getTotalPages(),donorListDTO);
//			}else {
//				throw new InvalidInputException("Invalid Input");
//			}
		
		 Specification<DonorDO> specification = (root, query, cb) -> {
		        List<Predicate> predicates = new ArrayList<>();
		        
		        if (bloodGroup != null && !bloodGroup.isEmpty()) {
		            predicates.add(root.get("bloodGroup").in(bloodGroup));
		        }

		        if (bloodBankName != null && !bloodBankName.isEmpty()) {
		        	 String pattern = "%" + bloodBankName + "%";
		            predicates.add(cb.like(root.get("bloodBankName"), pattern));
		        }

		        if (donationDate != null && !donationDate.isEmpty()) {
		            predicates.add(cb.equal(root.get("donationDate"), Utility.parseDate(donationDate)));
		        }
		        
		     

		        if (search != null && !search.isEmpty()) {
		            String searchString = "%" + search.toLowerCase() + "%";
		            List<Predicate> attributePredicates = new ArrayList<>();
		            for (SingularAttribute<? super DonorDO, ?> attribute : root.getModel().getSingularAttributes()) {
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

		    if (type.equals("HISTORY")) {
		        specification = specification.and((root, query, cb) -> cb.and(
		            root.get("status").in(List.of(DONOR_STATUS.CLOSE,DONOR_STATUS.CANCEL))
		        ));
		    } else if (type.equals("ACTIVE")) {  
		    	specification = specification.and((root, query, cb) -> cb.and(
		            root.get("status").in(List.of(DONOR_STATUS.ALLOCATED,DONOR_STATUS.PENDING,DONOR_STATUS.ACCEPTED,DONOR_STATUS.DONE))
		           
		        ));
		    } else {
		        throw new InvalidInputException("Invalid Input");
		    }
		   

		    donorList = donorRepository.findAll(specification, paging);
		    List<DonorDTO> donorListDTOList = Utility.mapList(donorList.getContent(), DonorDTO.class);
		    return new ResponseDTO<DonorDTO>(donorList.getTotalElements(), donorList.getTotalPages(), donorListDTOList);
	}

	@Override
	public ResponseDTO<DonorDTO> getByStatusAndAttenderId(String type, Integer pageNo, Integer pageSize, String sortBy, String search,String bloodBankName,List<String> bloodGroup,String donationDate) {
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
			
		Page<DonorDO> donorList;
//		if(type.equals("HISTORY")) {
//			donorList = donorRepository.findByStatusAndAttenderId(List.of(DONOR_STATUS.CLOSE,DONOR_STATUS.CANCEL),Utility.getSessionUser().getId(),search,paging);
//			 List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
//			 return   new ResponseDTO<DonorDTO>(donorList.getTotalElements(),donorList.getTotalPages(),donorListDTO);
//		}else if(type.equals("ACTIVE")) {
//			donorList =donorRepository.findByStatusAndAttenderId(List.of(DONOR_STATUS.ALLOCATED,DONOR_STATUS.PENDING,DONOR_STATUS.ACCEPTED,DONOR_STATUS.DONE),Utility.getSessionUser().getId(),search,paging);
//			 List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
//			 return   new ResponseDTO<DonorDTO>(donorList.getTotalElements(),donorList.getTotalPages(),donorListDTO);
//		}
//		else {
//			throw new InvalidInputException("Invalid Input");
//		}
		
		 Specification<DonorDO> specification = (root, query, cb) -> {
		        List<Predicate> predicates = new ArrayList<>();
		        
		        if (bloodGroup != null && !bloodGroup.isEmpty()) {
		        	predicates.add(root.get("bloodGroup").in(bloodGroup));
		        }

		        if (bloodBankName != null && !bloodBankName.isEmpty()) {
		        	String pattern = "%" + bloodBankName + "%";
		            predicates.add(cb.equal(root.get("bloodBankName"), pattern));
		        }

		        if (donationDate != null && !donationDate.isEmpty()) {
		            predicates.add(cb.equal(root.get("donationDate"), Utility.parseDate(donationDate)));
		        }
		        
		     

		        if (search != null && !search.isEmpty()) {
		            String searchString = "%" + search.toLowerCase() + "%";
		            List<Predicate> attributePredicates = new ArrayList<>();
		            for (SingularAttribute<? super DonorDO, ?> attribute : root.getModel().getSingularAttributes()) {
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

		    if (type.equals("HISTORY")) {
		        specification = specification.and((root, query, cb) -> cb.and(
		            root.get("status").in(List.of(DONOR_STATUS.CLOSE,DONOR_STATUS.CANCEL)),
		            cb.equal(root.get("userId"), Utility.getSessionUser().getId())
		        ));
		    } else if (type.equals("ACTIVE")) {  
		    	specification = specification.and((root, query, cb) -> cb.and(
		            root.get("status").in(List.of(DONOR_STATUS.ALLOCATED,DONOR_STATUS.PENDING,DONOR_STATUS.ACCEPTED,DONOR_STATUS.DONE)),
		            cb.equal(root.get("userId"), Utility.getSessionUser().getId())
		           
		        ));
		    } else {
		        throw new InvalidInputException("Invalid Input");
		    }
		   

		    donorList = donorRepository.findAll(specification, paging);
		    List<DonorDTO> donorListDTOList = Utility.mapList(donorList.getContent(), DonorDTO.class);
		    return new ResponseDTO<DonorDTO>(donorList.getTotalElements(), donorList.getTotalPages(), donorListDTOList);
	}

	@Override
	public ResultDTO updateById(Long id, DonorRequestUpdateDTO updateData) {
		Optional<DonorDO> donorDetails = donorRepository.findById(id);
		DonorDO data = donorDetails.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		data.setStatus(DONOR_STATUS.ALLOCATED);
		data.setLocation(updateData.getLocation());
		data.setBloodBankName(updateData.getBloodBankName());
		data.setDonationDate(updateData.getDonationDate());	
		data.setBloodRequest(null);
		donorRepository.save(data);
		executorService.execute(() -> {
			String title = "Donation";
			String deviceId = userDeviceIdService.getDeviceId(data.getUserId());	 
			String body =String.format("%s You can Donate Blood in Blood Bank %s.",
					data.getName(),
					updateData.getBloodBankName());
			
		
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
	public ResponseDTO<DonorDTO> donorByBloodGroup(String group,Integer pageNo, Integer pageSize, String sortBy) {
		if(group == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
		Page<DonorDO> donorList =donorRepository.findByBloodGroupAndStatusInAndBloodRequestIsNull(group,List.of(DONOR_STATUS.PENDING,DONOR_STATUS.ALLOCATED,DONOR_STATUS.ACCEPTED),paging);
		List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
		 return   new ResponseDTO<DonorDTO>(donorList.getTotalElements(),donorList.getTotalPages(),donorListDTO);
	}
    
	
	@Override
	public ResultDTO changeStatus(Long id,DONOR_STATUS status) {
		DONOR_STATUS newStatus = null;
		switch(status) {
		  case ALLOCATED:
			  newStatus = DONOR_STATUS.ALLOCATED;
			  donorRepository.findByIdAndUpdateStatus(id,newStatus);
		    break;
		  case ACCEPTED:
			  newStatus = DONOR_STATUS.ACCEPTED;
			  helperService.acceptOrCancelByDonor(id, true);
			  donorRepository.findByIdAndUpdateStatus(id,newStatus);
			  break;
		  case DONE:
			  newStatus = DONOR_STATUS.DONE;
			  donorRepository.findByIdAndUpdateStatus(id,newStatus);
			  break;
		  case CLOSE:
			  newStatus = DONOR_STATUS.CLOSE;
			  donorRepository.findByIdAndUpdateStatus(id,newStatus);
			  break;
		  case CANCEL:
			  newStatus = DONOR_STATUS.CANCEL;
			  helperService.acceptOrCancelByDonor(id, false);
			  donorRepository.findByIdAndUpdateStatus(id,newStatus);
			  break;
		  default:
			  throw new InvalidInputException("Invalid Input");
	}
		
		
		
		executorService.execute(() -> {
			String title = "Donation";
			String body = null;
			List<String> deviceId = new ArrayList<>();
			
			if(status.equals(DONOR_STATUS.ACCEPTED)) {
				  DonorDO donor = donorRepository.findById(id).orElse(null);
				  deviceId = userDeviceIdService.getAdminsAndDeviceId(); 
				  if(donor.getBloodRequest() != null) {
					  body = String.format("Donor %s is Ready To Donate Blood To %s",
		                      donor.getName(),donor.getBloodRequest().getName());
					  deviceId.add(userDeviceIdService.getDeviceId(donor.getBloodRequest().getId()));
				  }else {
					  body = String.format("Location %s Accepted By %s to Donate",
		                      donor.getLocation(),donor.getName());
				  }
				 
			
			}
			else if(status.equals(DONOR_STATUS.CANCEL)) {
				  DonorDO donor = donorRepository.findById(id).orElse(null);
				  deviceId = userDeviceIdService.getAdminsAndDeviceId(); 
				  if(donor.getBloodRequest() != null) {
					  body = String.format("Donor %s Cancel To Donate Blood To %s",
		                      donor.getName(),donor.getBloodRequest().getName());
					  deviceId.add(userDeviceIdService.getDeviceId(donor.getBloodRequest().getId()));
				  }else {
					  body = String.format("Donor %s Cancel The Donate Request",
		                     donor.getName());
				  }
				 
			
			}
			else if(status.equals(DONOR_STATUS.DONE)) {
				  DonorDO donor = donorRepository.findById(id).orElse(null);
				  body =String.format("Blood Donated by %s",
						  donor.getName());
				  deviceId = userDeviceIdService.getAdminsAndDeviceId();  
			
			}
			

			if(deviceId.size() > 0) {
				NotificationRequest notify = new NotificationRequest(title,body,deviceId);
				try {
					fcmService.sendMessageToToken(notify);
				} catch (FirebaseMessagingException | InterruptedException | ExecutionException e) {
					log.info("--------ERROR IN FIREBASE---------");
					e.printStackTrace();
					
				}
			}
			
		});
		return new ResultDTO(id.toString(),"Donor Status Change Successfully!");
	}


	@Override
	public ResponseDTO<DonorDTO> getAllDonor(Integer pageNo, Integer pageSize, String sortBy, String status,
			String startDate, String endDate, String search) {

	    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

	    Page<DonorDO> pageResult = donorRepository.findAll((root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (status != null && !status.isEmpty()) {
	            predicates.add(cb.equal(root.get("status"), ServiceConstant.DONOR_STATUS.valueOf(status)));
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
	            for (SingularAttribute<? super DonorDO, ?> attribute : root.getModel().getSingularAttributes()) {
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

	    List<DonorDTO> BloodRequestlist = Utility.mapList(pageResult.getContent(), DonorDTO.class);
	    return new ResponseDTO<DonorDTO>(pageResult.getTotalElements(), pageResult.getTotalPages(), BloodRequestlist);

	
	}

	
	@Override
	public ProfileResponseDTO uploadImage(ProfileUploadDTO profileUploadDTO) {
		DonorDO donor = donorRepository.findById(profileUploadDTO.getId()).orElse(null);
		if(donor == null) {
			 throw new InvalidInputException("No Donor Present");
		}
		
		var fileName = "donor/" + System.currentTimeMillis() +"_"+ profileUploadDTO.getImage().getOriginalFilename();
	
		System.out.println("user.getImage()-----"+donor.getImage()+profileUploadDTO.getImage().getOriginalFilename());
		System.out.println("fileName-----"+fileName);
		if(donor.getImage() != null) {
			cloudinaryService.delete(donor.getImage() );
		}
//		executorService.execute(() -> {
			 cloudinaryService.upload(profileUploadDTO.getImage(), fileName);

//		});
			 donor.setImage(fileName);
			 donorRepository.save(donor);

		return new ProfileResponseDTO(this.filePath+fileName);
	}
	
	

}
