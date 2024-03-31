package com.app.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.model.DonorDO;
import com.app.service.DonorService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/donate")
@Api(tags = { "donate" })
public class DonorController {

	@Autowired
	private DonorService donorService;

	@PostMapping("")
	public ResultDTO save(@RequestBody @Valid DonorRequestDTO donor) {

		return donorService.createRequest(donor);

	}



	// Admin
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	@GetMapping("/admin-list")
	public ResponseDTO<DonorDTO> getItemsByStatus(
    		@RequestParam  String type,
    		@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String search
            ) {
		return donorService.getByStatus(type,pageNo,pageSize,sortBy,search);

	}

	// Attender
	@GetMapping("/attender-list")
	public ResponseDTO<DonorDTO> attenderHistoryList(
    		@RequestParam  String type,
    		@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String search
            ) {
		return donorService.getByStatusAndAttenderId(type,pageNo,pageSize,sortBy,search);

	}

	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	@PutMapping("/update/{id}")
	public ResultDTO updateById(@PathVariable(name = "id") Long id, @RequestBody @Valid DonorRequestUpdateDTO updateData) {

		return donorService.updateById(id, updateData);

	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	@GetMapping("/blood-donor")
	public ResponseDTO<DonorDTO> donorByBloodGroup(
			@RequestParam String group,
			@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    
            ) {
        return donorService.donorByBloodGroup(group,pageNo,pageSize,sortBy);

	}
	
	 @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	 @GetMapping("/all")
	 public ResponseDTO<DonorDTO> getAllRequest(
	    		@RequestParam(defaultValue = "0") Integer pageNo,
               @RequestParam(defaultValue = "10") Integer pageSize,
               @RequestParam(defaultValue = "id") String sortBy,
               @RequestParam(required = false) String status,
               @RequestParam(required = false) String startDate,
               @RequestParam(required = false) String endDate,
               @RequestParam(required = false) String search) {
			return donorService.getAllDonor(pageNo,pageSize,sortBy,status,startDate,endDate,search);
		}
	
	@GetMapping("/{id}")
	public DonorDTO getDonorDetails(@PathVariable(name = "id") Long id) {

		return donorService.getDonorDetails(id);

	}
	
	@PutMapping("/updateStatus")
	public ResultDTO accept(@RequestParam("id") Long id,@RequestParam("status") DONOR_STATUS status) {
		return donorService.changeStatus(id,status);
		
 }
	
	

}
