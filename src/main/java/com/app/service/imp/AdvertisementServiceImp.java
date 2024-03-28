package com.app.service.imp;



import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.FileDTO;
import com.app.dto.FileUploadDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.exception.InvalidInputException;
import com.app.model.AdvertisementDO;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;
import com.app.model.UserDO;
import com.app.repository.AdvertisementRepository;
import com.app.service.AdvertisementService;
import com.app.service.FilesStorageService;
import com.app.utilities.Utility;
import org.springframework.context.annotation.PropertySource;
import io.jsonwebtoken.lang.Collections;
@Service
public class AdvertisementServiceImp implements AdvertisementService{
   
	@Autowired AdvertisementRepository advertisementRepository;
	
	@Autowired  FilesStorageService filesStorageService;
	
//	 @Autowired
//	 private Environment env;

//	    public String getFileUrl() {
//	        return env.getProperty("app.file.url");
//	    }

	    
	private final Path advertise = Paths.get("uploads/advertise");
	

	 private String filePath = "http://localhost:1996/uploads/"+"advertise/" ;
	 
	@Override
	public ResultDTO upload(FileUploadDTO fileUploadDTO) {
		var fileName = System.currentTimeMillis() +"_"+ fileUploadDTO.getFile().getOriginalFilename();
		
		filesStorageService.save(fileUploadDTO.getFile(),fileName , this.advertise);
		
		AdvertisementDO advertise = new AdvertisementDO();
		advertise.setName(fileUploadDTO.getName());
		advertise.setUrl(fileName);
		advertise.setStatus(STATUS.ACTIVE);
		advertisementRepository.save(advertise);
		return new ResultDTO("","Uploaded Successfully!");
	}

	@Override
	public ResultDTO update(FileUploadDTO updateData , Long id) {	
			Optional<AdvertisementDO> data = advertisementRepository.findById(id);
			AdvertisementDO b = data.orElse(null);
			if(b == null) {	
				throw new InvalidInputException("No Advertisement Present");
				}
			if(updateData.getFile() != null) {
				var fileName = System.currentTimeMillis() +"_"+ updateData.getFile().getOriginalFilename();
				filesStorageService.update(updateData.getFile() , fileName ,b.getUrl() , this.advertise);
				b.setUrl(fileName);
			}
			
 				b.setName(updateData.getName());
 				b.setStatus(updateData.getStatus());
 				advertisementRepository.save(b);
				return new ResultDTO(id.toString(),"Updated Successfully!");	
			
	}

	@Override
	public ResultDTO delete(Long id) {
		
			Optional<AdvertisementDO> data = advertisementRepository.findById(id);
			AdvertisementDO b = data.orElse(null);
			if(b == null) {
				throw new InvalidInputException("No Advertisement Present");
			}
			if(b.getUrl() != null) {
				filesStorageService.delete(b.getUrl(), this.advertise);
			}
			
			advertisementRepository.deleteById(id);
			return new ResultDTO(id.toString(),"Deleted Successfully!"); 
			
	   }
			

	
	
	@Override
	public FileDTO getById(Long id) {
		AdvertisementDO data = advertisementRepository.getOne(id);
//			Optional<AdvertisementDO> data = advertisementRepository.findById(id);
//			AdvertisementDO b = data.orElse(null);
			if(data == null) {
				throw new InvalidInputException("No Advertisement Present");
			}
			FileDTO result = Utility.mapObject(data,FileDTO.class);
			result.setUrl(this.filePath+result.getUrl());	
			return result;
	}

	@Override
	public List<FileDTO> latestAdvertise() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileDTO> allAdvertise() {
		List<AdvertisementDO> data = advertisementRepository.findByStatusOrderByCreatedAtDesc(STATUS.ACTIVE);
		List<FileDTO> result = Utility.mapList(data, FileDTO.class);
		// TODO Auto-generated method stub
		List<FileDTO> modifiedResult = result.stream().map(ads -> {
			ads.setUrl(this.filePath+ads.getUrl());
			return ads;
		}).collect(Collectors.toList());
		return modifiedResult;

	}




	



}
