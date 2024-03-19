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

import com.app.dto.BloodRequestDTO;
import com.app.model.BloodRequestDO;
import com.app.model.UserDO;
import com.app.service.BloodRequestService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/blood-request")
@Api(tags = {"blood-request"})
public class BloodRequestController {
	
	@Autowired
	 private BloodRequestService bloodRequestService ;
	
	@PostMapping("/save")
	public BloodRequestDO save(@RequestBody BloodRequestDO bloodRequest) {

		
		return bloodRequestService.createRequest(bloodRequest);
		
	}
	 
	@GetMapping("/{id}")
	public BloodRequestDTO getBloodRequestById(@PathVariable(name = "id") Long id) {

		
		return bloodRequestService.getById(id);
		
	}
	
	
	

}
