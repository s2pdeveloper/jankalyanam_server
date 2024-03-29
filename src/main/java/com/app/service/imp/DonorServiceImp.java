package com.app.service.imp;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ResultDTO;
import com.app.exception.InvalidInputException;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.repository.DonorRepository;
import com.app.service.DonorService;
import com.app.utilities.Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DonorServiceImp implements DonorService{
   
	@Autowired
    private DonorRepository donorRepository;

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
        donor.setBloodRequest(null);
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
			donorList =donorRepository.findByStatusAndAttenderId(List.of(DONOR_STATUS.ALLOCATED,DONOR_STATUS.REALLOCATED,DONOR_STATUS.PENDING,DONOR_STATUS.ACCEPTED,DONOR_STATUS.DONE),Utility.getSessionUser().getId(),search,paging);
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
			
		}
		
		data.setStatus(updateData.getStatus() == null ? data.getStatus() : updateData.getStatus());
		data.setLocation(updateData.getLocation());
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
    
	
	@Override
	public ResultDTO changeStatus(Long id,DONOR_STATUS status) {
		System.out.print("here------------");
		DONOR_STATUS newStatus = null;
		System.out.print("here------------");
		switch(status) {
		  case ALLOCATED:
			  newStatus = DONOR_STATUS.ALLOCATED;
		    break;
		  case REALLOCATED:
			  newStatus = DONOR_STATUS.REALLOCATED;
		    break;
		  case ACCEPTED:
			  newStatus = DONOR_STATUS.ACCEPTED;
			    break;
		  case DONE:
			  newStatus = DONOR_STATUS.DONE;
			  break;
		  case CLOSE:
			  newStatus = DONOR_STATUS.CLOSE;
			    break;
		  default:
			  throw new InvalidInputException("Invalid Input");
	}
		System.out.print("here------------");
		donorRepository.findByIdAndUpdateStatus(id,newStatus);
		return new ResultDTO(id.toString(),"Donor Status Change Successfully!");
	}

	
	
	
	

}
