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
import com.app.dto.ProfileResponseDTO;
import com.app.dto.ProfileUploadDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.model.DistrictDO;
import com.app.model.DonorDO;
import com.app.model.StateDO;
import com.app.model.TahsilDO;
import com.app.model.VillageDO;
import com.app.service.DonorService;
import com.app.service.HelperService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/address")
@Api(tags = { "address" })
public class AddressController {

	@Autowired
	private HelperService helperService;

	@PostMapping("/state")
	public ResultDTO insertState(@RequestBody  List<StateDO> states) {

		return helperService.bulkInsertState(states);

	}

	@PostMapping("/district")
	public ResultDTO insertDistrict(@RequestBody  List<DistrictDO> district) {

		return helperService.bulkInsertDistrict(district);

	}
	
	@PostMapping("/tahsil")
	public ResultDTO insertTahsil(@RequestBody  List<TahsilDO> tahsil) {

		return helperService.bulkInsertTahsil(tahsil);

	}
	
	@PostMapping("/village")
	public ResultDTO insertVillage(@RequestBody  List<VillageDO> village) {

		return helperService.bulkInsertVillage(village);

	}


	@GetMapping("/list")
	public List<StateDO> getList() {

		return helperService.getAll();

	}


	
}
