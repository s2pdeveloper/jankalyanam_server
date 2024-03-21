package com.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
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
	public ResultDTO save(@RequestBody DonorRequestDTO donor) {

		return donorService.createRequest(donor);

	}

	@GetMapping("/{id}")
	public DonorDTO getDonorDetails(@PathVariable(name = "id") Long id) {

		return donorService.getDonorDetails(id);

	}

	// Admin
	@GetMapping("/admin-list/{type}")
	public List<DonorDTO> getItemsByStatus(@PathVariable String type) {
		return donorService.getByStatus(type);

	}

	// Attender
	@GetMapping("/attender-list/{type}")
	public List<DonorDTO> attenderHistoryList(@PathVariable String type) {
		return donorService.getByStatusAndAttenderId(type);

	}

	@PutMapping("/update/{id")
	public ResultDTO updateById(@PathVariable(name = "id") Long id, @RequestBody DonorRequestUpdateDTO updateData) {

		return donorService.updateById(id, updateData);

	}

}
