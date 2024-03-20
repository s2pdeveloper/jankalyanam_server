package com.app.service.imp;

import java.util.* ;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.app.dto.BloodRequestDTO;
import com.app.dto.DonorDTO;
import com.app.dto.UserDTO;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.model.UserDO;
import com.app.repository.BloodRequestRepository;
import com.app.service.BloodRequestService;
import com.app.utilities.Utility;

@Service
public class BloodRequestServiceImp implements BloodRequestService{
   
	@Autowired
    private BloodRequestRepository bloodRequestRepository;

	@Override
	public BloodRequestDTO getById(Long id) {
		try {
		Optional<BloodRequestDO> data = bloodRequestRepository.findById(id);
		BloodRequestDO b = data.orElse(null);
		BloodRequestDTO result = Utility.mapObject(b,BloodRequestDTO.class);
		return result;
		}
		catch(Exception e) {
			System.out.println("EROORR__________"+e);
			 throw new RuntimeException("Failed to get blood request by ID: " + id, e);
		}
	
		
	}
	


	@Override
	public BloodRequestDTO createRequest(BloodRequestDO bloodRequest) {
	 BloodRequestDO blood  = bloodRequestRepository.save(bloodRequest);
	 return Utility.mapObject(blood, BloodRequestDTO.class);
	}
    
	@Override
    public List<BloodRequestDTO> getByStatus(String type) { 
		if(type.equals("HISTORY")) {
			List<BloodRequestDO> BloodRequestList = bloodRequestRepository.findByStatus("DONE");
			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
			 return BloodRequestlist ;
		}else if(type.equals("ACTIVE")) {
			List<BloodRequestDO> BloodRequestList =bloodRequestRepository.findByStatusIn(List.of("PENDING","ACCEPTED","RECEIVED"));
			 List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
			 return BloodRequestlist ;
		}else if(type.equals("MYLIST")) {

			
			//			TODO
//			List<BloodRequestDO> BloodRequestList = bloodRequestRepository.findStatusesByUserId()	;
//			List<BloodRequestDTO> BloodRequestlist = Utility.mapList(BloodRequestList, BloodRequestDTO.class);
//			 return BloodRequestlist ;
			
			return null ;
	
		}
//		TODO
		return null;
       
    }



	@Override
	public List<BloodRequestDTO> getAllRequest() {
		
		return null;
	}
	

    


}
