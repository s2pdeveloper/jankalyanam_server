package com.app.dto;


import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.PROVIDED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class DonorRequestUpdateDTO {
	
	@NotEmpty(message = "The Location is required.")
    private String location;
	
	@NotEmpty(message = "The Blood Bank Name is required.")
	private String BloodBankName;
	
	private LocalDate donationDate;
   
	private DONOR_STATUS status;

	
}