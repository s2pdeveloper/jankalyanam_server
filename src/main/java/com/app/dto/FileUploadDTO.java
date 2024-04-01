package com.app.dto;

import org.springframework.web.multipart.MultipartFile;

import com.app.constant.ServiceConstant.ADVERTISE_TYPE;
import com.app.constant.ServiceConstant.STATUS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class FileUploadDTO {
	
	 private String name;
	 
	 private MultipartFile file;
	 
	 private STATUS status;
	 
	 private ADVERTISE_TYPE type;
}
