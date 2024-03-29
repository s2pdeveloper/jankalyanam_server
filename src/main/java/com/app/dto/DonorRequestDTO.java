package com.app.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import com.app.constant.ServiceConstant.GENDER;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class DonorRequestDTO {
    
	@NotEmpty(message = "The Name is required.")
    private String name;
//	@NotEmpty(message = "The Age is required.")
    private int age;
	@NotEmpty(message = "The State is required.")
    private String state;
	@NotEmpty(message = "The City is required.")
    private String city;
	@NotEmpty(message = "The Mobile Number is required.")
    private String mobileNo;
//	@NotEmpty(message = "The Hemoglobin is required.")
    private double hemoglobin;
//	@NotEmpty(message = "The Illeness is required.")
    private Boolean illness;
//    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date donationDate;
    private GENDER gender;
	@NotEmpty(message = "The Blood Group is required.")
    private String bloodGroup;
}
