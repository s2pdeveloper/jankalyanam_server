package com.app.service.imp;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.repository.BloodRequestRepository;
import com.app.repository.DonationRepository;
import com.app.service.BloodRequestService;
import com.app.service.DonationService;

@Service
public class DonationServiceImp implements DonationService{
   
	@Autowired
    private DonationRepository donationRepository;

	@Override
	public Optional<DonorDO> getById(Long id) {
		try {
		Optional<DonorDO> data = donationRepository.findById(id);
		System.out.println("data_________"+data);
		return data;
		}
		catch(Exception e) {
			System.out.println("EROORR__________"+e);
			 throw new RuntimeException("Failed to get blood request by ID: " + id, e);
		}
	
		
	}

	@Override
	public DonorDO createRequest(DonorDO donation) {
	return donationRepository.save(donation);
	}
    
	



}
