package com.app.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.model.ChatMessageDO;
import com.app.repository.ChatMessageRepository;
import com.app.service.ChatMessageService;

public class ChatMessageServiceImp implements ChatMessageService{

	@Autowired
	private ChatMessageRepository messageRepository;
	  
	    @Override
	    public List<ChatMessageDO> getMessage(String room) {
	        return messageRepository.findAllByRoom(room);
	    }

	    @Override
	    public ChatMessageDO saveMessage(ChatMessageDO message) {
	        return messageRepository.save(message);
	    }
}
