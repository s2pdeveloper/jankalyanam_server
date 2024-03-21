package com.app.service.imp;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.app.dto.BloodRequestDTO;
import com.app.dto.FileDTO;
import com.app.dto.FileUploadDTO;
import com.app.dto.ResultDTO;
import com.app.model.AdvertisementDO;
import com.app.model.BloodRequestDO;
import com.app.repository.AdvertisementRepository;
import com.app.service.AdvertisementService;
import com.app.service.FilesStorageService;
import com.app.utilities.Utility;

@Service
public class AdvertisementServiceImp implements AdvertisementService{
   
	@Autowired AdvertisementRepository advertisementRepository;
	
	@Autowired  FilesStorageService filesStorageService;

	@Override
	public ResultDTO upload(FileUploadDTO fileUploadDTO) {
		
		var fileName = System.currentTimeMillis() +"_"+ fileUploadDTO.getFile().getOriginalFilename();
		filesStorageService.save(fileUploadDTO.getFile(),fileName);
		
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
		try {
			Optional<AdvertisementDO> data = advertisementRepository.findById(id);
			AdvertisementDO b = data.orElse(null);
			var result1 = filesStorageService.load(b.getUrl());
			System.out.println("result1----"+result1);
			FileDTO result = Utility.mapObject(b,FileDTO.class);
			return result;
			}
			catch(Exception e) {
				System.out.println("EROORR__________"+e);
				 throw new RuntimeException("Failed to get Advertisement by ID: " + id, e);
			}
	
	
	
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
