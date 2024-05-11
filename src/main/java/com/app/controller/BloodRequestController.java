package com.app.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.STATUS;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.MonthlyCountDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;

import com.app.service.BloodRequestService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/blood-request")
@Api(tags = {"blood-request"})
public class BloodRequestController {
	
	@Autowired
	 private BloodRequestService bloodRequestService ;
	
	@PostMapping("")
	public ResultDTO save(@RequestBody @Valid BloodDTO bloodRequest) {

		return bloodRequestService.createRequest(bloodRequest);
		
	}
	 

	//Admin
	 @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	 @GetMapping("/admin-list")
	    public ResponseDTO<BloodRequestDTO> getItemsByStatus(
	    		@RequestParam  String type,
	    		@RequestParam(defaultValue = "0") Integer pageNo,
                @RequestParam(defaultValue = "10") Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(required = false) String search
                ) {
	     return bloodRequestService.getByStatus(type,pageNo,pageSize,sortBy,search);
	      
	  }
	 
	//Attender
	 @GetMapping("/attender-list")
	    public ResponseDTO<BloodRequestDTO> attenderHistoryList(
	    		@RequestParam  String type,
	    		@RequestParam(defaultValue = "0") Integer pageNo,
                @RequestParam(defaultValue = "10") Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(required = false) String search
                ) {
	     return bloodRequestService.getByStatusAndAttenderId(type,pageNo,pageSize,sortBy,search);
	  }
	 
	@PutMapping("/update")
		public ResultDTO accept(@RequestParam("id") Long id,@RequestParam("status") BLOOD_STATUS status) {
			return bloodRequestService.acceptRequest(id,status);
			
	 }
	
	 
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	@PutMapping("/update/{id}")
		public ResultDTO updateById(@PathVariable(name = "id") Long id,@RequestBody BloodRequestUpdateDTO updateData) {

			return bloodRequestService.updateById(id,updateData);
			
	 }


	
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	@GetMapping("/all")
	public ResponseDTO<BloodRequestDTO> getAllRequest(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, 
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String search) {
		return bloodRequestService.getAllRequest(pageNo, pageSize, sortBy, status, startDate, endDate, search);
	}
	
	 
	@GetMapping("/analytics/monthly")
	public List<MonthlyCountDTO> getAnalyticsByYear(@RequestParam("year") Integer year,@RequestParam("status") String status) {
		
		 return bloodRequestService.getAnalyticsByYear(year,status);
	}

	@GetMapping("/{id}")
	public BloodRequestDTO getBloodRequestById(@PathVariable(name = "id") Long id) {
		
		return bloodRequestService.getById(id);
	}

	 
}
