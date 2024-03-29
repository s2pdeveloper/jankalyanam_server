package com.app.service.imp;

import java.util.* ;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.PROVIDED;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DonorDTO;
import com.app.dto.NotificationRequest;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.exception.InvalidInputException;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.model.UserDO;
import com.app.repository.BloodRequestRepository;
import com.app.service.BloodRequestService;
import com.app.service.DonorService;
import com.app.utilities.Utility;

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
	  
	@Override
	public BloodRequestDTO getById(Long id) {
//		NotificationRequest notify = new NotificationRequest("Title","Body","Topic","dQ0HVLFtQcy3NCcijiMaAk:APA91bE1l9i4e85sFkMdXuM-qE1BC8BTfIHYVTqeec6Fk6frUjR1YXk7b5R2nkqgqxA-nVTQQn3j7mH_7wSyox3lFDLjOH8MPh2w-UavYdRmfShpvD5QhNdDwT9PMHIUVovnwl8c18Av");
//		try {
//			fcmService.sendMessageToToken(notify);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
    public List<BloodRequestDTO> getByStatus(String type,Integer pageNo, Integer pageSize, String sortBy, String search) { 
	
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
		Slice<BloodRequestDO> BloodRequestList;
			if(type.equals("HISTORY")) {
				 BloodRequestList = bloodRequestRepository.findAllByStatus(List.of(BLOOD_STATUS.DONE),search,paging);
				 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
				 return BloodRequestlist ;
			}else if(type.equals("ACTIVE")) {
				 BloodRequestList =bloodRequestRepository.findAllByStatus(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.RECEIVED),search,paging);
				 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
				 return BloodRequestlist ;
			}else if(type.equals("MYLIST")) {
				
				BloodRequestList = bloodRequestRepository.findByStatusAndAdminId(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.RECEIVED),Utility.getSessionUser().getId(),search, paging);
				List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
				return BloodRequestlist ;
			
			}else {
				throw new InvalidInputException("Invalid Input");
			}
			

		
		
	
       
    }



	@Override
	public List<BloodRequestDTO> getAllRequest() {
		
		return null;
	}

	@Override
	public List<BloodRequestDTO> getByStatusAndAttenderId(String type,Integer pageNo, Integer pageSize, String sortBy, String search) {
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
		Slice<BloodRequestDO> BloodRequestList;
		if(type.equals("HISTORY")) {
			 BloodRequestList = bloodRequestRepository.findByStatusInAndAttenderId(List.of(BLOOD_STATUS.DONE),Utility.getSessionUser().getId(),search, paging);
			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
			 return BloodRequestlist ;
		}else if(type.equals("ACTIVE")) {
			 BloodRequestList =bloodRequestRepository.findByStatusInAndAttenderId(List.of(BLOOD_STATUS.PENDING,BLOOD_STATUS.ACCEPTED,BLOOD_STATUS.RECEIVED),Utility.getSessionUser().getId(),search, paging);
			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList.getContent(), BloodRequestDTO.class);
			 return BloodRequestlist ;
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
		
		return new ResultDTO(id.toString(),"Blood Request Accepted Successfully!");
	}

	@Override
	public ResultDTO updateById(Long id, BloodRequestUpdateDTO updateData) {
		Optional<BloodRequestDO> bloodRequest = bloodRequestRepository.findById(id);
		BloodRequestDO data = bloodRequest.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		
		if(updateData.getDonorId() == null && updateData.getBloodBankName() == null) {
			throw new InvalidInputException("Please Fill Atleast One Information");
		}
		if(data.getDonorId() != null) {
			donorService.assignOrRemoveToBloodRequest(data.getDonorId(),false);
		}
		if(updateData.getProvided().equals(PROVIDED.DONOR) && updateData.getDonorId() != null) {
		
			donorService.assignOrRemoveToBloodRequest(updateData.getDonorId(),true);
			data.setBloodBankName(null);
			data.setBankState(null);
			data.setState(null);
		}else {
			data.setBloodBankName(updateData.getBloodBankName());
			data.setBankState(updateData.getBankState());
			data.setState(updateData.getBankCity());
			data.setDonorId(null);		
			
		}
		data.setProvided(updateData.getProvided());
		data.setState(BLOOD_STATUS.ALLOCATED.name());
		bloodRequestRepository.save(data);
		return new ResultDTO(id.toString(),"Updated Successfully!");
		
	}


	

    


}
