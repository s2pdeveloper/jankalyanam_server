package com.app.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.* ;

//import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DonorDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.model.UserDO;
import com.app.repository.BloodRequestRepository;
import com.app.service.BloodRequestService;
import com.app.utilities.Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BloodRequestServiceImp implements BloodRequestService{
	
	private static Logger LOGGER = LoggerFactory.getLogger(BloodRequestServiceImp.class);
	
	@Autowired
    private BloodRequestRepository bloodRequestRepository;

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
		mapBloodRequest.setAttenderId(Utility.getSessionUser().getId());
	  bloodRequestRepository.save(mapBloodRequest);
	  return new ResultDTO("","Successfully Created!");
	  
	}
    
	@Override
    public List<BloodRequestDTO> getByStatus(String type) { 
		
		if(Utility.getSessionUser().getRole().equals(ROLE.ADMIN)) {
			if(type.equals("HISTORY")) {
				List<BloodRequestDO> BloodRequestList = bloodRequestRepository.findByStatus(BLOOD_STATUS.DONE);
				 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
				 return BloodRequestlist ;
			}else if(type.equals("ACTIVE")) {
				List<BloodRequestDO> BloodRequestList =bloodRequestRepository.findByStatusIn(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.RECEIVED));
				 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
				 return BloodRequestlist ;
			}else if(type.equals("MYLIST")) {
				
				List<BloodRequestDO> BloodRequestList = bloodRequestRepository.findByStatusAndAdminId(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.RECEIVED),Utility.getSessionUser().getId())	;
				List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
				 return BloodRequestlist ;
			
		
			}
			return null;

		}else {
		
			return null;
		}
		
       
    }



	@Override
	public List<BloodRequestDTO> getAllRequest() {
		
		return null;
	}

	@Override
	public List<BloodRequestDTO> getByStatusAndAttenderId(String type) {
		if(Utility.getSessionUser().getRole().equals(ROLE.ATTENDER)) {
		if(type.equals("HISTORY")) {
			List<BloodRequestDO> BloodRequestList = bloodRequestRepository.findByStatusAndAttenderId(List.of(BLOOD_STATUS.DONE),Utility.getSessionUser().getId());
			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
			 return BloodRequestlist ;
		}else if(type.equals("ACTIVE")) {
			List<BloodRequestDO> BloodRequestList =bloodRequestRepository.findByStatusAndAttenderId(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.RECEIVED),Utility.getSessionUser().getId());
			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
			 return BloodRequestlist ;
		}
		return null;
		}else {
			return null;
		}
	}

	@Override
	public ResultDTO acceptRequest(Long id,BLOOD_STATUS status) {
		BLOOD_STATUS changeStatus = null;
		switch(status) {
		  case ACCEPTED:
			  changeStatus = BLOOD_STATUS.ACCEPTED;
		    break;
		  case PENDING:
			  changeStatus = BLOOD_STATUS.PENDING;
		    break;
		  case RECEIVED:
			  changeStatus = BLOOD_STATUS.RECEIVED;
			    break;
		  case DONE:
			  changeStatus = BLOOD_STATUS.DONE;
			    break;
		  default:
		     
		}
		bloodRequestRepository.findByIdAndUpdateStatus(id,changeStatus);
		return new ResultDTO(id.toString(),"Blood Request Accepted Successfully!");
	}

	@Override
	public ResultDTO updateById(Long id, BloodRequestUpdateDTO updateData) {
		Optional<BloodRequestDO> bloodRequest = bloodRequestRepository.findById(id);
		BloodRequestDO data = bloodRequest.orElse(null);
		if(data == null) {
			
		}
		
		data.setProvided(updateData.getProvided());
		data.setDonorId(updateData.getDonorId() != null ? updateData.getDonorId() : data.getDonorId());
		bloodRequestRepository.save(data);
		return new ResultDTO(id.toString(),"Updated Successfully!");
		
	}



	@Override
	public List<BloodRequestDO> findNearbyBloodRequests() {
		System.out.println("HERE");
		  LocalDateTime currentDateTime = LocalDateTime.now();
	        LocalDateTime oneDayBeforeDateTime = currentDateTime.minusDays(4);
	        System.out.println(oneDayBeforeDateTime) ;
	        System.out.println(currentDateTime) ;
		List<BloodRequestDO> BloodRequestList =bloodRequestRepository.findByCreatedAtBetween(oneDayBeforeDateTime, currentDateTime);
//		 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
//		 return null ;
//		log.info(BloodRequestList);
		System.out.print(BloodRequestList.toString());
//		LOGGER.info(BloodRequestList+"This is a test" + bloodRequestRepository.findByCreatedAtBetween(oneDayBeforeDateTime, currentDateTime));
		return null;
	}
	

    


}
