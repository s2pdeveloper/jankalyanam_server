package com.app.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.model.BloodRequestDO;
import com.app.model.UserDO;
import com.app.service.BloodRequestService;

import io.swagger.annotations.Api;
import com.app.model.*;

@RestController
@RequestMapping("/blood-request")
@Api(tags = {"blood-request"})
public class BloodRequestController {
	
	@Autowired
	 private BloodRequestService bloodRequestService ;
	
	@PostMapping("")
	public ResultDTO save(@RequestBody BloodDTO bloodRequest) {

		return bloodRequestService.createRequest(bloodRequest);
		
	}
	 

	//Admin
	 @RolesAllowed("ADMIN")
	 @GetMapping("/admin-list")
	    public List<BloodRequestDTO> getItemsByStatus(
	    		@RequestParam  String type,
	    		@RequestParam(defaultValue = "0") Integer pageNo,
                @RequestParam(defaultValue = "10") Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(required = false) String searchBy,
                @RequestParam(required = false) String search
                ) {
	     return bloodRequestService.getByStatus(type,pageNo,pageSize,sortBy,searchBy,search);
	      
	  }
	 
	//Attender
	 @GetMapping("/attender-list/{type}")
	    public List<BloodRequestDTO> attenderHistoryList(@PathVariable String type) {
	     return bloodRequestService.getByStatusAndAttenderId(type);
	      
	  }
	 
	@PutMapping("/update")
		public ResultDTO accept(@RequestParam("id") Long id,@RequestParam("status") BLOOD_STATUS status) {

			return bloodRequestService.acceptRequest(id,status);
			
	 }
	
	 
	@PutMapping("/update/{id}")
		public ResultDTO updateById(@PathVariable(name = "id") Long id,@RequestBody BloodRequestUpdateDTO updateData) {

			return bloodRequestService.updateById(id,updateData);
			
	 }

	@RolesAllowed("ROLE_ATTENDER")
	@GetMapping("/{id}")
	public BloodRequestDTO getBloodRequestById(@PathVariable(name = "id") Long id) {
		
		return bloodRequestService.getById(id);
	}
//	 @GetMapping("/all")
//		public List<BloodRequestDO> getAllRequest() {
//			return bloodRequestService.getAllRequest();
//		}
	

	 
}
