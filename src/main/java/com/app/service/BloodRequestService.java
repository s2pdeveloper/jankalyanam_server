package com.app.service;

import java.util.* ;

import org.springframework.stereotype.Service;

import com.app.dto.BloodRequestDTO;
import com.app.dto.UserDTO;
import com.app.model.BloodRequestDO;


@Service
public interface BloodRequestService {

    public BloodRequestDTO getById(Long id);
    public BloodRequestDTO createRequest(BloodRequestDO bloodRequest);
    public List<BloodRequestDTO> getByStatus(String Status) ;
	 public List<BloodRequestDTO> getAllRequest();

}