package com.app.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.GENDER;
import com.app.constant.ServiceConstant.PROVIDED;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class ResponseDTO<T>  {

	private Long count;
	
	private Integer pages;
	
	private List<T> data;

}