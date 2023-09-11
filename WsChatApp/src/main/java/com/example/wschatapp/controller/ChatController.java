package com.example.wschatapp.controller;

import com.example.wschatapp.entity.Chat;
import com.example.wschatapp.handler.ChatWebSocketHandler;
import com.example.wschatapp.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*",exposedHeaders = "msg")
@RestController
@RequestMapping("/messages")
public class ChatController {

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;
    @Autowired
    private ChatService chatService;

    @GetMapping("/geMsgsByUser")
    public List<Chat> geMsgsByUser(@RequestParam String user) {
        return chatService.geMsgsByUser(user);
    }

    @GetMapping("/setMessagesRead")
    public void setMessagesRead(@RequestParam String user1, @RequestParam String user2) {
        chatService.setMessagesRead(user1, user2);
    }

    @PostMapping("/deleteMsg")
    public void deleteMsg(@RequestBody Chat msg){
        chatService.deleteMsg(msg);
    }

}
