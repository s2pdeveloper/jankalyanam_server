package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AdminsDeviceDTO;
import com.app.dto.ResultDTO;
import com.app.service.UserDeviceIdService;

@RestController
@RequestMapping("UserDeviceId")
public class UserDeviceIdController {

	@Autowired UserDeviceIdService userDeviceService ;
	
	

	@PostMapping("")
	public ResultDTO save (@RequestParam("deviceId") String deviceId) {
		return userDeviceService.save(deviceId) ;
	}


	

}
