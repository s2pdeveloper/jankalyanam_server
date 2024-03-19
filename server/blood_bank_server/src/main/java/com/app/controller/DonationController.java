package com.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.DonorDO;
import com.app.service.DonationService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/donate")
@Api(tags = {"donate"})
public class DonationController {
	
	@Autowired
	 private DonationService donationService ;
	
	@PostMapping("/save")
	public DonorDO save(@RequestBody DonorDO donation) {

		
		return donationService.createRequest(donation);
		
	}
	 
	@GetMapping("/{id}")
	public Optional<DonorDO> getDonationById(@PathVariable(name = "id") Long id) {

		
		return donationService.getById(id);
		
	}
	
	
	

}
