package com.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.FileDTO;
import com.app.dto.FileUploadDTO;
import com.app.dto.ResultDTO;
import com.app.model.DonorDO;
import com.app.service.AdvertisementService;
import com.app.service.DonorService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/advertise")
@Api(tags = {"advertise"})
public class AdvertisementController {
	
	@Autowired
	 private AdvertisementService advertisementService;
	
	@PostMapping("")
	public ResultDTO save(FileUploadDTO fileUploadDTO) {
		
		return advertisementService.upload(fileUploadDTO);
		
	}
	
	@PutMapping("")
	public ResultDTO update(FileUploadDTO fileUploadDTO) {
		
		return advertisementService.update(fileUploadDTO);
		
	}
	 
	
	@DeleteMapping("/{id}")
	public ResultDTO delete(@PathVariable(name = "id") Long id) {
		
		return advertisementService.delete(id);
		
	}
	
	@GetMapping("/{id}")
	public FileDTO getById(@PathVariable(name = "id") Long id) {
		
		return advertisementService.getById(id);
		
	}
	
	@GetMapping("/all")
	public List<FileDTO> getAll() {
		
		return advertisementService.allAdvertise();
		
	}
	
	@GetMapping("/latest")
	public List<FileDTO> latestData() {
		
		return advertisementService.latestAdvertise();
		
	}
	

}
