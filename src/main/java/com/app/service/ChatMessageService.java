package com.app.service;

import java.util.List;

import com.app.model.ChatMessageDO;

public interface ChatMessageService {

	List<ChatMessageDO> getMessage(String room);

	ChatMessageDO saveMessage(ChatMessageDO message);

}
