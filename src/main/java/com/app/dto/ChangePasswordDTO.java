package com.app.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
	
	@NotEmpty(message = "The New Password is required.")
	private String newPassword;
	
	@NotEmpty(message = "The Password is required.")
	private String password;
}
