package com.app.service.imp;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.app.dto.FileDTO;
import com.app.dto.FileUploadDTO;
import com.app.dto.ResultDTO;
import com.app.model.AdvertisementDO;
import com.app.repository.AdvertisementRepository;
import com.app.service.AdvertisementService;
import com.app.service.FilesStorageService;

@Service
public class AdvertisementServiceImp implements AdvertisementService{
   
	@Autowired AdvertisementRepository advertisementRepository;
	
	@Autowired  FilesStorageService filesStorageService;

	@Override
	public ResultDTO upload(FileUploadDTO fileUploadDTO) {
		var fileName = fileUploadDTO.getFile().getOriginalFilename() +"_"+ System.currentTimeMillis();
		filesStorageService.save(fileUploadDTO.getFile(), fileName);
		
		AdvertisementDO advertise = new AdvertisementDO();
		advertise.setName(fileUploadDTO.getName());
		advertise.setUrl(fileName);
		
		advertisementRepository.save(advertise);
	
		return new ResultDTO("","Uploaded Successfully!");
	}

	@Override
	public ResultDTO update(FileUploadDTO fileUploadDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDTO delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileDTO getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileDTO> latestAdvertise() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileDTO> allAdvertise() {
		// TODO Auto-generated method stub
		return null;
	}


	



}
