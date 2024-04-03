package com.app.service.imp;



import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.app.service.CloudinaryService;
import com.app.service.FilesStorageService;
import com.app.utilities.Utility;
import org.springframework.context.annotation.PropertySource;
import io.jsonwebtoken.lang.Collections;
@Service
public class AdvertisementServiceImp implements AdvertisementService{
   
	@Autowired AdvertisementRepository advertisementRepository;
	
	@Autowired CloudinaryService cloudinaryService;
	
	@Autowired
	@Qualifier("cachedThreadPool")
	private ExecutorService executorService;
	
	@Value("${cloudinary.url}")
	private String filePath;
	
	 
	@Override
	public ResultDTO upload(FileUploadDTO fileUploadDTO) {
		var fileName = "advertise/" + System.currentTimeMillis() +"_"+ fileUploadDTO.getFile().getOriginalFilename().substring(0,fileUploadDTO.getFile().getOriginalFilename().lastIndexOf('.'));
		AdvertisementDO advertise = new AdvertisementDO();
		advertise.setName(fileUploadDTO.getName());
		advertise.setUrl(fileName);
		advertise.setStatus(STATUS.ACTIVE);
		advertisementRepository.save(advertise);
		
//		executorService.execute(() -> {
			 cloudinaryService.upload(fileUploadDTO.getFile(), fileName);

//		});
		
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
				var fileName = "advertise/" + System.currentTimeMillis() +"_"+ updateData.getFile().getOriginalFilename().substring(0,updateData.getFile().getOriginalFilename().lastIndexOf('.'));
				cloudinaryService.update(updateData.getFile() , fileName ,b.getUrl());
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
			
			advertisementRepository.deleteById(id);
			
			if(b.getUrl() != null) {
//				executorService.execute(() -> {
				cloudinaryService.delete(b.getUrl());
//				});
			}
	
			return new ResultDTO(id.toString(),"Deleted Successfully!"); 
			
	   }
			

	
	
	@Override
	public FileDTO getById(Long id) {
			Optional<AdvertisementDO> data = advertisementRepository.findById(id);
			AdvertisementDO b = data.orElse(null);
			if(b == null) {
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
