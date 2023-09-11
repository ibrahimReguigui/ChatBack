package com.example.wschatapp.service;

import com.example.wschatapp.entity.Chat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ChatService {
    List<Chat> geMsgsByUser(String user);
    void setMessagesRead(String user1,String user2);
    void deleteMsg(Chat msg);
}
