package com.app.dto;

import com.app.constant.ServiceConstant.ADVERTISE_TYPE;
import com.app.constant.ServiceConstant.STATUS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class FileDTO {
	
	 private Long id;
	 
	 private String name;
	 
	 private String url;
	 
	 private ADVERTISE_TYPE type ;
	 
	 private STATUS status;
}
