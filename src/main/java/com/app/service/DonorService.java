package com.app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ResultDTO;
import com.app.model.DonorDO;


@Service
public interface DonorService {

    public DonorDTO getDonorDetails(Long id);
    
    public ResultDTO createRequest(DonorRequestDTO donorRequestDTO);

	public List<DonorDTO> getByStatus(String type);

	public List<DonorDTO> getByStatusAndAttenderId(String type);

	public ResultDTO updateById(Long id, DonorRequestUpdateDTO updateData);
}