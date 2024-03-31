package com.app.dto;

import java.util.List;
import java.util.Map;

import com.app.constant.ServiceConstant.PROVIDED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class NotificationRequest {

	
//	   private Map<String,String> data;
	   private String title;
	   private String body;
	    private List<String> tokens;
	    
}
