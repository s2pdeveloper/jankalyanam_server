package com.app.dto;

import java.time.LocalDate;
import java.util.Date;

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
public class BloodRequestDTO {

private	Long id;
private	String bloodGroup;
private GENDER gender;
private String fatherOrHusband;
private	Long mobileNo;
private	String location;
private	String illness;
private	double hemoglobin;
private	String city;
private	String state;
private	int age;
private	String name;
private	PROVIDED provided;
//@JsonFormat(pattern="dd-MM-yyyy")
private	LocalDate bloodRequireDate;
private	BLOOD_STATUS status;
private	int units;
private String bloodBankName;
private String bankState;  
private String bankCity;
private	UserDTO acceptor;
private	UserDTO attender;
private	DonorRequestDTO donor;
	
}