package com.app.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.app.constant.ServiceConstant.ROLE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
	
	@NotEmpty(message = "The First Name is required.")
    private String firstName;
	
	@NotEmpty(message = "The lastName is required.")
    private String lastName;
    
	@NotEmpty(message = "The mobileNois required.")
    private String  mobileNo;
    
	@NotEmpty(message = "The Password is required.")
    private String  password;
	
	@NotEmpty(message = "The blood Group is required.")
	private String bloodGroup;
	
	private LocalDate  DOB;
	
    private ROLE role;
 
    @NotEmpty(message = "The State is required.")
    private String  state;
    
    @NotEmpty(message = "The District is required.")
    private String  district;
    
    private String  tahsil;
    
    private String  village;
    
    private Long  pincode;
}
