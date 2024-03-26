package com.app.dto;

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
	
    private ROLE role;
}
