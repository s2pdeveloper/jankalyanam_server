package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dto.AdminsDeviceDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.ResultDTO;


@Service
public interface UserDeviceIdService {

    public ResultDTO save(String deviceid);
    
    public List<AdminsDeviceDTO> getAdminsAndDeviceId() ;
}
