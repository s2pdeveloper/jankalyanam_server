package com.app.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	  
	  @Override
	  public void addCorsMappings(CorsRegistry registry) {

	    registry.addMapping("/**")
	      .allowedOrigins("*")
	      .allowedMethods("GET", "POST","PUT","DELETE","PATCH")
	      .allowedHeaders("*");
	  


	  }

	  	@Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	      
    	  registry.addResourceHandler("/uploads/**")
          .addResourceLocations("file:./uploads/");
	    	  
	      registry.addResourceHandler("swagger-ui.html")
	      .addResourceLocations("classpath:/META-INF/resources/");

	      registry.addResourceHandler("/webjars/**")
	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
	    }

}
