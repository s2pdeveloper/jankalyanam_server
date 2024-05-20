package com.app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.model.ChatMessageDO;


@Service
public interface SocketService {

    public ChatMessageDO processMessage(ChatMessageDO message);
    
    public List<ChatMessageDO> getMessageList(String room);
  
}