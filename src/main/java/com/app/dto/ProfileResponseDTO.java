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
public class ProfileResponseDTO {
	
	 private String image;
	
}
