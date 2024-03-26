package com.app.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.app.dto.ApiError;
import com.app.utilities.Utility;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class AuthenticationExceptionHandler {

	public void handleUnAuthorizedError(HttpServletResponse response,Exception e,String message)
	{
	    ApiError error = null;
	    if(e!=null)
	        error = new ApiError(e.getMessage(),message);
	    else
	    	 error = new ApiError("",message);
	  
   	 response.setContentType("application/json");
	 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	 try {
		response.getOutputStream().println(Utility.objectToString(error));
	} catch (JsonProcessingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	}
}
