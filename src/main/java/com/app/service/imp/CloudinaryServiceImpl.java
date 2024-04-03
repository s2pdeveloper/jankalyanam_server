package com.app.service.imp;

import org.springframework.web.multipart.MultipartFile;

import com.app.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

	 @Resource
	 private Cloudinary cloudinary;
	 
	@Override
	public void upload(MultipartFile file, String folderName) {
		  try{
	            HashMap<Object, Object> options = new HashMap<>();
	            options.put("public_id", folderName);
	            options.put("overwrite", true);

	            cloudinary.uploader().upload(file.getBytes(), options);
	         

	        }catch (IOException e){
	            e.printStackTrace();
	            System.out.println("ERROR------"+e);
	        }
		
	}

	@Override
	public void delete(String folderName) {
		  try{
	           
//		      HashMap<Object, Object> options = new HashMap<>();
//	            options.put("public_id", folderName);
			  cloudinary.uploader().destroy(folderName, null);
	     
	        }catch (IOException e){
	            e.printStackTrace();
	        }
		
	}

	@Override
	public void update(MultipartFile file, String newName, String oldName) {
		  delete(oldName);
		  upload(file,newName);
		
		
	}

}
