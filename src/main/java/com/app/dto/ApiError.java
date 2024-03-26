package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiError {

//	private String id;

	private String error;
	private String message;
//	private String errorType;

//	public ApiError(String error, String message) {
//		this.message = message;
//		this.error = error;
//	}

}
