package com.app.dto;

import java.util.Date;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.PROVIDED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class BloodRequestDTO {
	
private	String bloodGroup;
private	Long mobileNo;
private	String location;
private	String illness;
private	double hemoglobin;
private	String city;
private	String state;
private	int age;
private	String name;
private	PROVIDED provided;
private	Date bloodRequireDate;
private	BLOOD_STATUS status;
private	int units;
private	UserDTO acceptor;
private	UserDTO attender;
private	DonorDTO donor;
	
}
