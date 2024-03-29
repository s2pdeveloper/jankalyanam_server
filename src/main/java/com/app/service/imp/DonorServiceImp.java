package com.app.service.imp;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ResultDTO;
import com.app.exception.InvalidInputException;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.repository.BloodRequestRepository;
import com.app.repository.DonorRepository;
import com.app.service.DonorService;
import com.app.utilities.Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DonorServiceImp implements DonorService{
   
	@Autowired
    private DonorRepository donorRepository;
	
	@Autowired
    private BloodRequestRepository bloodRequestRepository;

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
	public ResultDTO createRequest(DonorRequestDTO donorRequestDTO) {
		DonorDO donor = Utility.mapObject(donorRequestDTO, DonorDO.class);
		donor.setUserId(Utility.getSessionUser().getId());
		DonorDO donorSave = donorRepository.save(donor);
		return new ResultDTO(donorSave.getId().toString(),"Save Successfully!");
	 
	}

	@Override
	public List<DonorDTO> getByStatus(String type, Integer pageNo, Integer pageSize, String sortBy, String search) {
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
			
		Slice<DonorDO> donorList;
		if(type.equals("HISTORY")) {
				donorList = donorRepository.findByStatusIn(List.of(DONOR_STATUS.CLOSE),search,paging);
				 List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
				 return donorListDTO ;
			}else if(type.equals("ACTIVE")) {
				 donorList =donorRepository.findByStatusIn(List.of(DONOR_STATUS.PENDING,DONOR_STATUS.DONE),search,paging);
				 List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
				 return donorListDTO ;
			}else {
				throw new InvalidInputException("Invalid Input");
			}
	}

	@Override
	public List<DonorDTO> getByStatusAndAttenderId(String type, Integer pageNo, Integer pageSize, String sortBy, String search) {
		if(type == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
			
		Slice<DonorDO> donorList;
		if(type.equals("HISTORY")) {
			donorList = donorRepository.findByStatusAndAttenderId(List.of(DONOR_STATUS.CLOSE),Utility.getSessionUser().getId(),search,paging);
			 List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
			 return donorListDTO ;
		}else if(type.equals("ACTIVE")) {
			donorList =donorRepository.findByStatusAndAttenderId(List.of(DONOR_STATUS.PENDING,DONOR_STATUS.DONE),Utility.getSessionUser().getId(),search,paging);
			 List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
			 return donorListDTO ;
		}
		else {
			throw new InvalidInputException("Invalid Input");
		}
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
		data.setBloodRequest(null);
		donorRepository.save(data);
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
		return new ResultDTO(id.toString(),"Updated Successfully!");
	}

	
	@Override
	public List<DonorDTO> donorByBloodGroup(String group,Integer pageNo, Integer pageSize, String sortBy) {
		if(group == null) {
			throw new InvalidInputException("Invalid Input");
		}

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()); 
		Slice<DonorDO> donorList =donorRepository.findByBloodGroupAndStatusIn(group,List.of(DONOR_STATUS.PENDING,DONOR_STATUS.DONE),paging);
		List<DonorDTO> donorListDTO = Utility.mapList(donorList.getContent(), DonorDTO.class);
		return donorListDTO;
	}
    
	
	

}
