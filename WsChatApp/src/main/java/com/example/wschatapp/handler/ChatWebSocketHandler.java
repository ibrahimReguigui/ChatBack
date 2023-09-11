package com.example.wschatapp.handler;

import com.example.wschatapp.entity.Chat;
import com.example.wschatapp.repo.ChatRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatRepository chatRepository;
    private static final List<String> connectedUsers = new ArrayList<>();
    private static final List<WebSocketSession> webSocketSessions = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();


    private void broadcastUsersStatus(String username, boolean isConnected) throws IOException {

        if (isConnected == true)
            connectedUsers.add(username);
        else
            connectedUsers.remove(username);

        String usersConnectedJson = "{\"type\":\"status\",\"status\":\"connected\",\"users\":\""
                + connectedUsers + "\"}";
        if (webSocketSessions.size() != 0) {
            log.warn(String.valueOf(webSocketSessions.size()));
            for (WebSocketSession webSocketSession : webSocketSessions) {
                webSocketSession.sendMessage(new TextMessage(usersConnectedJson));
            }
        }
    }

    public WebSocketSession getSessionByUserName(String userName) {
        for (WebSocketSession session : webSocketSessions) {
            String recipient = session.getUri().toString().split("=")[1].split(",")[0];
            if (recipient.equals(userName))
                return session;
        }
        return null;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //String loggedInUser =session.getUri().getQuery().split("=")[1]
        String loggedInUser = session.getUri().toString().split("=")[1].split(",")[0];
        webSocketSessions.add(session);
        broadcastUsersStatus(loggedInUser, true);

        log.warn(loggedInUser + " connected \nadded web sockets :" + session + "\nthere are "
                + webSocketSessions.size() + " websockets");

    }

    public void messageDeletedNotif(Chat chat) {
        String messageDeletedNotif = "{\"type\":\"notification\",\"status\":\"msgdeleted\"}";
        if(connectedUsers.contains(chat.getRecipient())){
            log.info("User conected sending notif...");
            webSocketSessions.stream().filter(e->
                    e.getUri().toString().split("=")[1].split(",")[0].equals(chat.getRecipient()))
                    .forEach(e-> {
                        try {
                            e.sendMessage(new TextMessage(messageDeletedNotif));
                            log.warn("notif sent !!!");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        };
                    });
        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.warn(message.toString());
        log.warn(message.getPayload().toString());
        log.warn(session.toString());

        JsonNode jsonNode = objectMapper.readTree(message.getPayload().toString());

        //String loggedInUser = session.getUri().toString().split("=")[1].split(",")[0];
        String loggedInUser = jsonNode.get("user1").asText();
        //String content = message.getPayload().split(",")[2].split(":")[1].substring(1,
          //      message.getPayload().split(",")[2].split(":")[1].length() - 1);
        String content =jsonNode.get("message").asText();
        //String rawRecipientName = message.getPayload().split(",")[1].split(":")[1];
        //String recipient = rawRecipientName.substring(1, rawRecipientName.length() - 1);
        String recipient =jsonNode.get("user2").asText();
//        String url = message.getPayload().split(",")[3]
//                .split(":")[1].substring(0, message.getPayload().split(",")[3]
//                .split(":")[1].length());
        String url =jsonNode.get("url").asText();
        LocalDateTime time = LocalDateTime.now();

        Chat newMessage = new Chat();
        newMessage.setContent(content);
        newMessage.setSender(loggedInUser);
        newMessage.setRecipient(recipient);
        newMessage.setTime(time);

        String modifiedPayload = Arrays.stream(message.getPayload().split(",")).limit(4)
                .collect(Collectors.joining(","))
                + ",\"time\":\"" + time + "\","
                + Arrays.stream(message.getPayload().split(","))
                .skip(5)
                .collect(Collectors.joining(","));

        TextMessage jsonMessage = new TextMessage(modifiedPayload);

        if (!url.equals("null")) {
            newMessage.setUrl(url);
        } else {
            newMessage.setUrl(null);
        }

        chatRepository.save(newMessage);

        if (getSessionByUserName(recipient) != null)
            getSessionByUserName(recipient).sendMessage(jsonMessage);
        getSessionByUserName(loggedInUser).sendMessage(jsonMessage);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String loggedInUser = session.getUri().toString().split("=")[1].split(",")[0];
        webSocketSessions.remove(session);
        broadcastUsersStatus(loggedInUser, false);

        log.info(loggedInUser + " disconnected\nremains :" + connectedUsers + "\nthere are " + webSocketSessions.size() + " websockets");
    }
}
