package com.app.service;

import java.util.* ;

import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.model.BloodRequestDO;


@Service
public interface BloodRequestService {

    public BloodRequestDTO getById(Long id);
    
    public ResultDTO createRequest(BloodDTO bloodRequest);
    
    public List<BloodRequestDTO> getByStatus(String type, Integer pageNo, Integer pageSize, String sortBy, String searchBy, String search) ;
    
    public List<BloodRequestDTO> getByStatusAndAttenderId(String type) ;
    
	 public List<BloodRequestDTO> getAllRequest();

	 public ResultDTO acceptRequest(Long id, BLOOD_STATUS status);

	public ResultDTO updateById(Long id, BloodRequestUpdateDTO updateData);

}