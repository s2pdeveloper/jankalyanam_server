package com.app;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.app.service.FilesStorageService;


@SpringBootApplication
@ComponentScan
public class JankalyanamApplication  {

	 @Autowired
	  FilesStorageService storageService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(JankalyanamApplication.class, args);
	}

	
//     @Override
//	public void run(String... arg) throws Exception {
////	    storageService.deleteAll();
//	    storageService.init();
//	  }
}
