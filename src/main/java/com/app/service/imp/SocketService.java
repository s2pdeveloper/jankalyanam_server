package com.app.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.ChatMessageDO;
import com.app.service.ChatMessageService;
import com.app.utilities.Utility;
import com.corundumstudio.socketio.SocketIOClient;

import lombok.RequiredArgsConstructor;

@Service
public class SocketService {

	@Autowired
    private ChatMessageService chatMessageService;

    public void sendSocketmessage(SocketIOClient senderClient, ChatMessageDO message, String room) {
        for (
            SocketIOClient client: senderClient.getNamespace().getRoomOperations(room).getClients()
        ) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message", message);
            }
        }
    }

    public void saveMessage(SocketIOClient senderClient, ChatMessageDO message) {

    	message.setSenderId(Utility.getSessionUser().getId());
    	
    	ChatMessageDO storedMessage = chatMessageService.saveMessage(
    			message	
        );

        sendSocketmessage(senderClient, storedMessage, message.getRoom());

    }

    public void saveInfoMessage(SocketIOClient senderClient, String message, String room) {
    	ChatMessageDO chat = new ChatMessageDO();
    	chat.setRoom(room);
    	chat.setMessage(message);
    	ChatMessageDO storedMessage = chatMessageService.saveMessage(
    			chat
        );

        sendSocketmessage(senderClient, storedMessage, room);
    }
}
