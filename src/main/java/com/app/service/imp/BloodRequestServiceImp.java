package com.app.service.imp;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.app.dto.BloodRequestDTO;
import com.app.dto.DonorDTO;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
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
	public BloodRequestDO createRequest(BloodRequestDO bloodRequest) {
	return bloodRequestRepository.save(bloodRequest);
	}
    
	



}
