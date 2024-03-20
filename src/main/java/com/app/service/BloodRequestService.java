package com.app.service;



import org.springframework.stereotype.Service;

import com.app.dto.BloodRequestDTO;
import com.app.model.BloodRequestDO;


@Service
public interface BloodRequestService {

    public BloodRequestDTO getById(Long id);
    public BloodRequestDO createRequest(BloodRequestDO bloodRequest);
}