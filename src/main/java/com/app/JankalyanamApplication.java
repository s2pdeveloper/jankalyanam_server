package com.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.app.service.FilesStorageService;
import com.app.service.UserService;
import com.corundumstudio.socketio.SocketIOServer;


@SpringBootApplication
@ComponentScan
public class JankalyanamApplication implements CommandLineRunner{

	 @Autowired
	 UserService userService;
	
	 @Autowired
	 SocketIOServer server;
	 
	public static void main(String[] args) {
		
	 SpringApplication.run(JankalyanamApplication.class, args);

	}
     

     @Override
	public void run(String... arg) throws Exception {
    	server.start();
 		userService.checkSuperAdmin();
//	    storageService.deleteAll();
//	    storageService.init();
	  }
     
	 
     
}
