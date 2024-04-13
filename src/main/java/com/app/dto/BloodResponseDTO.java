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
public class BloodResponseDTO {
	
private	String bloodGroup;
private	Long mobileNo;
private	String location;
private	String illness;
private	double hemoglobin;
private	String city;
private	String state;
private	int age;
private	String name;
@JsonFormat(pattern="dd-MM-yyyy")
private	LocalDate bloodRequireDate;
private	int units;
private GENDER gender;
private Boolean isWebsite;
private String fatherOrHusband;

	
}