package com.app.utilities;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.model.ChatMessageDO;
import com.app.service.imp.SocketService;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketModule {
		@Autowired
		private SocketIOServer server;

		@Autowired
	    private SocketService socketService;

	    public SocketModule(SocketIOServer server, SocketService socketService) {
	        this.server = server;
	        this.socketService = socketService;
	        server.addConnectListener(this.onConnected());
	        server.addDisconnectListener(this.onDisconnected());
	        server.addEventListener("send_message", ChatMessageDO.class, this.onChatReceived());
	    }

	    private DataListener<ChatMessageDO> onChatReceived() {
	        return (senderClient, data, ackSender) -> {
	            log.info(data.toString());
	            socketService.saveMessage(senderClient, data);
	        };
	    }

	    private ConnectListener onConnected() {
	        return (client) -> {
	            var params = client.getHandshakeData().getUrlParams();
	            String room = params.get("room").stream().collect(Collectors.joining());
	            String username = params.get("username").stream().collect(Collectors.joining());
	            client.joinRoom(room);
	            socketService.saveInfoMessage(client, String.format("Welcome", username), room);
	            log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username);
	        };

	    }

	    private DisconnectListener onDisconnected() {
	        return client -> {
	            var params = client.getHandshakeData().getUrlParams();
	            String room = params.get("room").stream().collect(Collectors.joining());
	            String username = params.get("username").stream().collect(Collectors.joining());
	            socketService.saveInfoMessage(client, String.format("Disconnected", username), room);
	            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
	        };
	    }

}
