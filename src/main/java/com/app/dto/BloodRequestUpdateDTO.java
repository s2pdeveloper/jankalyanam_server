package com.app.dto;


import javax.persistence.Column;

import com.app.constant.ServiceConstant.PROVIDED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class BloodRequestUpdateDTO {
	
	 private Long donorId;
	 
	 private PROVIDED provided;

	 private String bloodBankName;
	    
	 private String bankState;
	    
	 private String bankCity;
	
}