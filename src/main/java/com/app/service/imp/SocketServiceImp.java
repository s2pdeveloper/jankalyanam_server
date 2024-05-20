package com.app.service.imp;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.app.model.ChatMessageDO;
import com.app.repository.ChatMessageRepository;
import com.app.repository.DonorRepository;
import com.app.service.SocketService;

@Component
public class SocketServiceImp implements SocketService {

	@Autowired
    private ChatMessageRepository chatMessageRepository;
	
//	@Override
//	public void handleTextMessage(WebSocketSession session, TextMessage message)
//			throws InterruptedException, IOException {
//
//		System.out.println("Inside handleTextMessage-------");
//		String payload = message.getPayload();
//		System.out.println("payload handleTextMessage-------"+payload);
////		JSONObject jsonObject = new JSONObject(payload);
//		session.sendMessage(new TextMessage("Hi " + payload + " how may we help you?"));
//	}
	
	

	@Override
	public ChatMessageDO processMessage(ChatMessageDO message) {
		chatMessageRepository.save(message);
		return message;
	}

	@Override
	public List<ChatMessageDO> getMessageList(String room) {
		List<ChatMessageDO> chat = chatMessageRepository.findByRoomOrderBycreatedAtAsc(room);
		return chat;
	}

}
