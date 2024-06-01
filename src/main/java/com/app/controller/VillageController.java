package com.app.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.app.dto.DistrictDTO;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ProfileResponseDTO;
import com.app.dto.ProfileUploadDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.StateDTO;
import com.app.dto.TahsilDTO;
import com.app.dto.TahsilRequestDTO;
import com.app.dto.TahsilResponseDTO;
import com.app.dto.VillageDTO;
import com.app.dto.VillageRequestDTO;
import com.app.dto.VillageResponseDTO;
import com.app.model.DistrictDO;
import com.app.model.DonorDO;
import com.app.model.StateDO;
import com.app.model.TahsilDO;
import com.app.model.VillageDO;
import com.app.service.DonorService;
import com.app.service.HelperService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/village")
@Api(tags = { "village" })
public class VillageController {

	@Autowired
	private HelperService helperService;

	@PostMapping("")
	public ResultDTO addVillage(@RequestBody  VillageRequestDTO village) {

		return helperService.addVillage(village);

	}
	
	@PutMapping("/{id}")
	public ResultDTO updateVillage(@PathVariable(name = "id") Long id,@RequestBody VillageRequestDTO village) {
		
		return helperService.updateVillage(id,village);

	}
	
	@DeleteMapping("/{id}")
	public ResultDTO deleteVillage(@PathVariable(name = "id") Long id) {
		
		return helperService.deleteVillage(id);

	}

	@GetMapping("/all")
	public ResponseDTO<VillageResponseDTO> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, 
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(required = false) String tahsilId,
			@RequestParam(required = false) String search) {
		
		return helperService.getAllVillage(pageNo,pageSize,sortBy,tahsilId,search);

	}
	
	@GetMapping("/{id}")
	public VillageDTO getVillageById(@PathVariable(name = "id") Long id) {
		
		return helperService.getVillageById(id);

	}


	
}
