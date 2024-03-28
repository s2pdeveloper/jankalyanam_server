package com.app.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
	
	@NotEmpty(message = "The Mobile Number is required.")
	private String mobileNo;
	
	@NotEmpty(message = "The Paswword is required.")
	private String password;
}
