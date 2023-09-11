package com.example.wschatapp.service;

import com.example.wschatapp.entity.Chat;
import com.example.wschatapp.handler.ChatWebSocketHandler;
import com.example.wschatapp.repo.AttachementRepository;
import com.example.wschatapp.repo.ChatRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChatSerrviceImp implements ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private AttachementRepository attachementRepository;
    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public List<Chat> geMsgsByUser(String user) {
        return chatRepository.findAllBySenderOrRecipient(user, user);
    }

    @Override
    public void setMessagesRead(String user1, String user2) {
        chatRepository.findConversation(user1.trim(), user2.trim()).forEach(e -> {
            e.setRead(true);
            chatRepository.save(e);
        });
    }

    @Override
    @Transactional
    public void deleteMsg(Chat msg) {
        Chat chat = chatRepository.findChatBySenderAndRecipientAndContent(
                        msg.getSender(), msg.getRecipient(), msg.getContent()).
                stream().filter(e -> e.getTime().toString().substring(0, 23)
                        .equals(msg.getTime().toString().substring(0, 23))).findFirst().orElse(null);
        if (chat == null) {
            System.out.println("Message not found !!!");
            return;
        }
        if (chat.getRead() == true) {
            chat.setContent("<Message deleted>");
            chatRepository.save(chat);
            System.out.println("Message updated !!!");
        } else {
            log.info(chat.toString());
            chatRepository.deleteById(chat.getId());
            log.info(chat.toString());
            System.out.println("Message deleted !!!");
        }
        if (msg.getUrl() != null) {
            attachementRepository.delete(
                    attachementRepository.findById(Long.valueOf(msg.getUrl().trim())).get());
        }
        chatWebSocketHandler.messageDeletedNotif(chat);
    }
}
