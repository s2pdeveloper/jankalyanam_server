package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dto.FileDTO;
import com.app.dto.FileUploadDTO;
import com.app.dto.ResultDTO;


@Service
public interface AdvertisementService {

	public ResultDTO upload(FileUploadDTO fileUploadDTO);
	
//	public ResultDTO update(FileUploadDTO fileUploadDTO);
	
	public ResultDTO delete(Long id);
	
	public FileDTO getById(Long id);
	
	public List<FileDTO> latestAdvertise();
	
	public List<FileDTO> allAdvertise();

	public ResultDTO update(FileUploadDTO fileUploadDTO, Long id);
	
}