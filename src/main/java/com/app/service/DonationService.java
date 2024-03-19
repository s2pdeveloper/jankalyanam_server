package com.app.service;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.model.DonorDO;


@Service
public interface DonationService {

    public Optional<DonorDO> getById(Long id);
    
    public DonorDO createRequest(DonorDO bloodRequest);
}