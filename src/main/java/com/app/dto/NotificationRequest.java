package com.app.dto;

import com.app.constant.ServiceConstant.PROVIDED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class NotificationRequest {

	   private String title;
	    private String body;
	    private String topic;
	    private String token;
	    
}
