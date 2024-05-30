package com.app.service;

import java.util.* ;

import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.STATUS;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.MonthlyCountDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.model.BloodRequestDO;


@Service
public interface BloodRequestService {

    public BloodRequestDTO getById(Long id);
    
    public ResultDTO createRequest(BloodDTO bloodRequest);
    
    public ResponseDTO<BloodRequestDTO> getByStatus(String type, Integer pageNo, Integer pageSize, String sortBy,  String search, List<String> bloodType, List<String> bloodGroup, String hospitalName) ;
    
    public ResponseDTO<BloodRequestDTO> getByStatusAndAttenderId(String type, Integer pageNo, Integer pageSize, String sortBy
			, String search, List<String> bloodType, List<String> bloodGroup, String hospitalName);
    
	 public ResponseDTO<BloodRequestDTO> getAllRequest(Integer pageNo, Integer pageSize, String sortBy,String status,String startDate,String endDate ,String search);

	 public ResultDTO acceptRequest(Long id, BLOOD_STATUS status);

	public ResultDTO updateById(Long id, BloodRequestUpdateDTO updateData);

	public List<MonthlyCountDTO>  getAnalyticsByYear(Integer year, String status);

	

}