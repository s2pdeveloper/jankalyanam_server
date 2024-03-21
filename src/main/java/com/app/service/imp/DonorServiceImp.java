package com.app.service.imp;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ResultDTO;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.repository.DonorRepository;
import com.app.service.DonorService;
import com.app.utilities.Utility;

@Service
public class DonorServiceImp implements DonorService{
   
	@Autowired
    private DonorRepository donorRepository;

	@Override
	public DonorDTO getDonorDetails(Long id) {
		Optional<DonorDO> donorDetails = donorRepository.findById(id);
		DonorDO data = donorDetails.orElse(null);
		if(data == null) {
			
		}
		return Utility.mapObject(data, DonorDTO.class);
		
		
	}

	@Override
	public ResultDTO createRequest(DonorRequestDTO donorRequestDTO) {
		DonorDO donor = Utility.mapObject(donorRequestDTO, DonorDO.class);
		donor.setUserId(Utility.getSessionUser().getId());
		DonorDO donorSave = donorRepository.save(donor);
		return new ResultDTO(donorSave.getId().toString(),"Save Successfully!");
	 
	}

	@Override
	public List<DonorDTO> getByStatus(String type) {
		if(Utility.getSessionUser().getRole().equals(ROLE.ADMIN)) {
			if(type.equals("HISTORY")) {
				List<DonorDO> donorList = donorRepository.findByStatus(DONOR_STATUS.CLOSE);
				 List<DonorDTO> donorListDTO = Utility.mapList(donorList, DonorDTO.class);
				 return donorListDTO ;
			}else if(type.equals("ACTIVE")) {
				List<DonorDO> donorList =donorRepository.findByStatusIn(List.of(DONOR_STATUS.PENDING,DONOR_STATUS.DONE));
				 List<DonorDTO> donorListDTO = Utility.mapList(donorList, DonorDTO.class);
				 return donorListDTO ;
			}
			return null;

		}else {
		
			return null;
		}
	}

	@Override
	public List<DonorDTO> getByStatusAndAttenderId(String type) {
		if(Utility.getSessionUser().getRole().equals(ROLE.ATTENDER)) {
		if(type.equals("HISTORY")) {
			List<DonorDO> donorList = donorRepository.findByStatusAndAttenderId(List.of(DONOR_STATUS.CLOSE),Utility.getSessionUser().getId());
			 List<DonorDTO> donorListDTO = Utility.mapList(donorList, DonorDTO.class);
			 return donorListDTO ;
		}else if(type.equals("ACTIVE")) {
			List<DonorDO> donorList =donorRepository.findByStatusAndAttenderId(List.of(DONOR_STATUS.PENDING,DONOR_STATUS.DONE),Utility.getSessionUser().getId());
			 List<DonorDTO> donorListDTO = Utility.mapList(donorList, DonorDTO.class);
			 return donorListDTO ;
		}
		return null;
		}else {
			return null;
		}
	}

	@Override
	public ResultDTO updateById(Long id, DonorRequestUpdateDTO updateData) {
		Optional<DonorDO> donorDetails = donorRepository.findById(id);
		DonorDO data = donorDetails.orElse(null);
		if(data == null) {
			
		}
		
		data.setStatus(updateData.getStatus() == null ? data.getStatus() : updateData.getStatus());
		data.setLocation(updateData.getLocation());
		donorRepository.save(data);
		return new ResultDTO(id.toString(),"Updated Successfully!");
	}
    
	
	

}
