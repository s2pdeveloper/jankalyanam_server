package com.app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ResultDTO;
import com.app.model.DonorDO;


@Service
public interface DonorService {

    public DonorDTO getDonorDetails(Long id);
    
    public ResultDTO createRequest(DonorRequestDTO donorRequestDTO);

	public List<DonorDTO> getByStatus(String type, Integer pageNo, Integer pageSize, String sortBy, String search);

	public List<DonorDTO> getByStatusAndAttenderId(String type, Integer pageNo, Integer pageSize, String sortBy, String search);

	public ResultDTO updateById(Long id, DonorRequestUpdateDTO updateData);

	public List<DonorDTO> donorByBloodGroup(String group,Integer pageNo, Integer pageSize, String sortBy);

	ResultDTO changeStatus(Long id, DONOR_STATUS status);
	
	ResultDTO assignOrRemoveToBloodRequest(Long id,Boolean allocate);
}