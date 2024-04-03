package com.app.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfiguration {

		@Value("${cloudinary.cloud_name}")
	    private String cloudName;

	    @Value("${cloudinary.api_key}")
	    private String apiKey;

	    @Value("${cloudinary.api_secret}")
	    private String apiSecret;

	    @Bean
	    public Cloudinary cloudinary() {
	    	System.out.println("cloudName-------"+cloudName+"uuuuuu----"+apiKey+apiSecret);
	        return new Cloudinary(ObjectUtils.asMap(
	                "cloud_name", cloudName,
	                "api_key", apiKey,
	                "api_secret", apiSecret));
	    }
}
