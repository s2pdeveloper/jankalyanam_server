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
import com.app.constant.ServiceConstant.PROVIDED;
import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DonorDTO;
import com.app.dto.MonthlyCountDTO;
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
import com.app.service.HelperService;
import com.app.service.UserDeviceIdService;
import com.app.utilities.Utility;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Slf4j
public class BloodRequestServiceImp implements BloodRequestService{
   
	@Autowired
    private BloodRequestRepository bloodRequestRepository;

	@Autowired
	private FCMService fcmService;
	
	@Autowired
	private HelperService helperService;
	
	@Autowired
	@Qualifier("cachedThreadPool")
	private ExecutorService executorService;
	
	@Autowired
	private UserDeviceIdService userDeviceIdService;
	
    @Autowired
	private EntityManager entityManager;
	
	@Override
	public BloodRequestDTO getById(Long id) {
		Optional<BloodRequestDO> data = bloodRequestRepository.findById(id);
		BloodRequestDO b = data.orElse(null);
		BloodRequestDTO result = Utility.mapObject(b,BloodRequestDTO.class);
		return result;
//		NotificationRequest notify = new NotificationRequest("Need Blood","Pooja Dabi needs A+ blood in Nagpur",Arrays.asList("em0tQVkoRc-yrJJQEIfdAk:APA91bHQODi3zR2hlkeOphXrjUhYyTWIKCVBeMhLdr1S-JomuZeHfAmuC0DyO-0OqoePaDO3DNoEMDLEQ8_76OsDRq0KXkATDNj30jkymaQRB9MHx3n_E1-3uerS2q2ZfuPG2Dnl9RwR"));
//		try {
//			fcmService.sendMessageToToken(notify);
//		} catch (FirebaseMessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
		
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
			List<String> deviceIds = new ArrayList<>();
			deviceIds.addAll(userDeviceIdService.getAdminsAndDeviceId());
			deviceIds.addAll(userDeviceIdService.getAttenterByBloodGroup(bloodRequest.getBloodGroup()));
			
			if(deviceIds.size() > 0) {
				String title = "Blood Request";
				String body = String.format("New Request for %s Blood Group By %s Location: %s",
                        bloodRequest.getBloodGroup(),
                        bloodRequest.getName(),
                        bloodRequest.getHospitalName());
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
    public ResponseDTO<BloodRequestDTO> getByStatus(String type,Integer pageNo, Integer pageSize, String sortBy, String search,List<String> bloodType, List<String> bloodGroup, String hospitalName) { 
	
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Page<BloodRequestDO> bloodRequestList;
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
//		Page<BloodRequestDO> BloodRequestList;
//			if(type.equals("HISTORY")) {
//				 BloodRequestList = bloodRequestRepository.findAllByStatus(List.of(BLOOD_STATUS.DONE,BLOOD_STATUS.CANCEL),search,paging);
//				 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
//				 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
//			}else if(type.equals("ACTIVE")) {
//				 BloodRequestList =bloodRequestRepository.findAllByStatus(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.ALLOCATED,BLOOD_STATUS.RECEIVED),search,paging);
//				 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
//				 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
//			}else if(type.equals("MYLIST")) {
//				
//				BloodRequestList = bloodRequestRepository.findByStatusAndAdminId(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.ALLOCATED,BLOOD_STATUS.RECEIVED),Utility.getSessionUser().getId(),search, paging);
//				List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
//				 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
//			
//			}else {
//				throw new InvalidInputException("Invalid Input");
//			}
	
		   Specification<BloodRequestDO> specification = (root, query, cb) -> {
		        List<Predicate> predicates = new ArrayList<>();
		        
		        if (bloodGroup != null && !bloodGroup.isEmpty()) {
		        	predicates.add(root.get("bloodGroup").in(bloodGroup));
		        }

		        if (bloodType != null && !bloodType.isEmpty()) {
		        	predicates.add(root.get("bloodType").in(bloodType));
		        }

		        if (hospitalName != null && !hospitalName.isEmpty()) {
		            predicates.add(cb.equal(root.get("hospitalName"), hospitalName));
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
		    };

		    if (type.equals("HISTORY")) {
		        specification = specification.and((root, query, cb) -> cb.and(
		            root.get("status").in(List.of(BLOOD_STATUS.DONE, BLOOD_STATUS.CANCEL))
		        ));
		    } else if (type.equals("ACTIVE")) {  
		    	specification = specification.and((root, query, cb) -> cb.and(
		            root.get("status").in(List.of(BLOOD_STATUS.PENDING, BLOOD_STATUS.ACCEPTED, BLOOD_STATUS.ALLOCATED, BLOOD_STATUS.RECEIVED))
		           
		        ));
		    }else if (type.equals("MYLIST")) {  
		    	specification = specification.and((root, query, cb) -> cb.and(
			            root.get("status").in(List.of(BLOOD_STATUS.PENDING, BLOOD_STATUS.ACCEPTED, BLOOD_STATUS.ALLOCATED, BLOOD_STATUS.RECEIVED)),
			            cb.equal(root.get("acceptorId"), Utility.getSessionUser().getId())
			           
			        ));
			  } else {
		        throw new InvalidInputException("Invalid Input");
		    }
		   

		    bloodRequestList = bloodRequestRepository.findAll(specification, paging);
		    List<BloodRequestDTO> bloodRequestDTOList = Utility.mapList(bloodRequestList.getContent(), BloodRequestDTO.class);
		    return new ResponseDTO<BloodRequestDTO>(bloodRequestList.getTotalElements(), bloodRequestList.getTotalPages(), bloodRequestDTOList);
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

//	@Override
//	public ResponseDTO<BloodRequestDTO> getByStatusAndAttenderId(String type,Integer pageNo, Integer pageSize, String sortBy, String search,String bloodType, String bloodGroup, String hospitalName) {
//		if(type == null) {
//			throw new InvalidInputException("Invalid Input");
//		}
//
//		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
//		Page<BloodRequestDO> BloodRequestList;
//		if(type.equals("HISTORY")) {
//			 BloodRequestList = bloodRequestRepository.findByStatusInAndAttenderId(List.of(BLOOD_STATUS.DONE,BLOOD_STATUS.CANCEL),Utility.getSessionUser().getId(),search, paging);
//			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
//			 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
//		}else if(type.equals("ACTIVE")) {
//			 BloodRequestList =bloodRequestRepository.findByStatusInAndAttenderId(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.ALLOCATED,BLOOD_STATUS.RECEIVED),Utility.getSessionUser().getId(),search, paging);
//			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
//			 return new ResponseDTO<BloodRequestDTO>(BloodRequestList.getTotalElements(),BloodRequestList.getTotalPages(),BloodRequestlist);
//		}
//		else {
//			throw new InvalidInputException("Invalid Input");
//		}
//		
//	}
	
	@Override
	public ResponseDTO<BloodRequestDTO> getByStatusAndAttenderId(String type,Integer pageNo, Integer pageSize, String sortBy, String search,List<String> bloodType, List<String> bloodGroup, String hospitalName) {
	  
	    if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}
	    
	   
	    Page<BloodRequestDO> bloodRequestList;
	    Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
	    Specification<BloodRequestDO> specification = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();
	        
	        if (bloodGroup != null && !bloodGroup.isEmpty()) {
	        	predicates.add(root.get("bloodGroup").in(bloodGroup));
	        }

	        if (bloodType != null && !bloodType.isEmpty()) {
	        	predicates.add(root.get("bloodType").in(bloodType));
//	            predicates.add(cb.equal(root.get("bloodType"), ServiceConstant.BLOOD_TYPE.valueOf(bloodType)));
	        }

	        if (hospitalName != null && !hospitalName.isEmpty()) {
	            predicates.add(cb.equal(root.get("hospitalName"), hospitalName));
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
	    };

	    if (type.equals("HISTORY")) {
	        specification = specification.and((root, query, cb) -> cb.and(
	            root.get("status").in(List.of(BLOOD_STATUS.DONE, BLOOD_STATUS.CANCEL)),
	            cb.equal(root.get("attenderId"), Utility.getSessionUser().getId())
	        ));
	    } else if (type.equals("ACTIVE")) {  
	    	specification = specification.and((root, query, cb) -> cb.and(
	            root.get("status").in(List.of(BLOOD_STATUS.PENDING, BLOOD_STATUS.ACCEPTED, BLOOD_STATUS.ALLOCATED, BLOOD_STATUS.RECEIVED)),
	            cb.equal(root.get("attenderId"), Utility.getSessionUser().getId())
	        ));
	    } else {
	        throw new InvalidInputException("Invalid Input");
	    }
	   

	    bloodRequestList = bloodRequestRepository.findAll(specification, paging);
	    List<BloodRequestDTO> bloodRequestDTOList = Utility.mapList(bloodRequestList.getContent(), BloodRequestDTO.class);
	    return new ResponseDTO<BloodRequestDTO>(bloodRequestList.getTotalElements(), bloodRequestList.getTotalPages(), bloodRequestDTOList);
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
		  case CANCEL:
			  changeStatus = BLOOD_STATUS.CANCEL;
			  helperService.CancelBloodRequest(id);
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
				  System.out.println("deviceId-------"+deviceId);
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
				helperService.assignOrRemoveToBloodRequest(data.getDonorId(),false);
			});
			
		}
		if(updateData.getProvided().equals(PROVIDED.DONOR) && updateData.getDonorId() != null) {
		
			helperService.assignOrRemoveToBloodRequest(updateData.getDonorId(),true);
			data.setDonorId(updateData.getDonorId());
			data.setBloodBankName(null);
			data.setBankState(null);
			data.setBankCity(null);
			
		}else {
			data.setBloodBankName(updateData.getBloodBankName());
			data.setBankState(updateData.getBankState());
			data.setBankCity(updateData.getBankCity());
			data.setDonorId(null);		
			data.setStatus(BLOOD_STATUS.ALLOCATED);
		}
		data.setProvided(updateData.getProvided());
		
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

	@Override
	public List<MonthlyCountDTO> getAnalyticsByYear(Integer year, String status) {

		   CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
	        Root<BloodRequestDO> root = cq.from(BloodRequestDO.class);

	        Expression<Integer> yearExpression = cb.function("YEAR", Integer.class, root.get("createdAt"));
	        Predicate yearPredicate = cb.equal(yearExpression, year);

	        Predicate statusPredicate = cb.equal(root.get("status"), ServiceConstant.BLOOD_STATUS.valueOf(status));

	        Expression<Integer> monthExpression = cb.function("MONTH", Integer.class, root.get("createdAt"));
	        cq.multiselect(monthExpression, cb.count(root));
	        cq.where(cb.and(yearPredicate, statusPredicate));
	        cq.groupBy(monthExpression);
	        cq.orderBy(cb.asc(monthExpression));

	        TypedQuery<Object[]> query = entityManager.createQuery(cq);
	        List<Object[]> result = query.getResultList();

	        return convertResultsToDto(result);
	    }

	    private List<MonthlyCountDTO> convertResultsToDto(List<Object[]> results) {
	       
	        String[] months = {"January", "February", "March", "April", "May", "June",
	                           "July", "August", "September", "October", "November", "December"};
	        
	        List<MonthlyCountDTO> monthlyCounts = new ArrayList<>(months.length);
	        for (String month : months) {
	            monthlyCounts.add(new MonthlyCountDTO(month, 0));
	        }
	        		
	        for (Object[] result : results) {
	            int monthIndex = (Integer) result[0] - 1; // MONTH function returns 1 for January
	            long count = (Long) result[1];
	            monthlyCounts.set(monthIndex, new MonthlyCountDTO(months[monthIndex], count));
	        }
	        return monthlyCounts;
	    }


}
