package com.app.dto;

import java.time.LocalDate;
import java.util.Date;

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
public class BloodDTO {

	@NotEmpty(message = "The Blood Group is required.")	
private	String bloodGroup;
//	@NotEmpty(message = "The Mobile Number is required.")
private	Long mobileNo;
	@NotEmpty(message = "The Location is required.")
private	String location;
	@NotEmpty(message = "The Illeness is required.")
private	String illness;
//	@NotEmpty(message = "The Hemoglobin is required.")
private	double hemoglobin;
	@NotEmpty(message = "The City is required.")
private	String city;
	@NotEmpty(message = "The State is required.")
private	String state;
//	@NotEmpty(message = "The Age is required.")
private	int age;
	@NotEmpty(message = "The Name is required.")
private	String name;
//    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
private	LocalDate bloodRequireDate;
//	@NotEmpty(message = "The Units of Blood Required is required.")
private	int units;
private GENDER gender;
private Boolean isWebsite;

private String fatherOrHusband;

	
}