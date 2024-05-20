package com.app.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.model.ChatMessageDO;
import com.app.model.DonorDO;
import com.app.service.DonorService;
import com.app.service.SocketService;

import io.swagger.annotations.Api;


@RestController
@Api(tags = { "socket" })
public class socketController {

	@Autowired
	private SocketService socketService;


	// Attender
	@GetMapping("/chat/{room}")
	public List<ChatMessageDO> getChatList(@PathVariable(name = "room") String room)
          {
		return socketService.getMessageList(room);
	}


	
	@MessageMapping("/send")
    @SendTo("/receive/msg")
    public ChatMessageDO processEventName(ChatMessageDO message) throws Exception {
		return socketService.processMessage(message);
    }
	

}
