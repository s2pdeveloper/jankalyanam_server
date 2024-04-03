package com.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

	  public void upload(MultipartFile file, String folderName);
	  
	  public void delete(String folderName);

	  public void update(MultipartFile file, String newName, String oldName);
}
