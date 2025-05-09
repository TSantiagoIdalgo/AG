package com.ancore.ancoregaming.websocket.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MessageService extends TextWebSocketHandler {
  private final Logger logger = Logger.getLogger(MessageService.class.getName());
  public enum MessageTypes {
    PAYMENT, NEW_PAYMENT_RECEIVED
  }
  private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
  @Autowired
  private ObjectMapper objectMapper;
  
  @Override
  public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
    Principal user = session.getPrincipal();
    if (user != null) {
      sessions.put(user.getName(), session);
    } else {
      session.close(CloseStatus.BAD_DATA);
    }
  }
  
  @Override
  public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
    Principal user = session.getPrincipal();
    if (user != null) {
      sessions.remove(user.getName());
    }
  }
  
  public void sentToUser(String userId, MessageTypes type, Object payload) {
    WebSocketSession session = sessions.get(userId);
    if (session != null && session.isOpen()) {
      try {
        WebSocketMessage message = new WebSocketMessage(type, payload);
        String json = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(json));
      } catch (IOException e) {
        logger.log(Level.WARNING, "Error sending to user: " + userId);
      }
    }
  }
  
  public void broadcast(MessageTypes type, Object payload) {
    try {
      WebSocketMessage message = new WebSocketMessage(type, payload);
      String json = objectMapper.writeValueAsString(message);
      sessions.values().forEach(session -> {
        if (session.isOpen()) {
          try {
            session.sendMessage(new TextMessage(json));
          } catch (IOException e) {
            logger.log(Level.WARNING, "Error to broadcasting to user");
          }
        }
      });
    } catch (JsonProcessingException | RuntimeException e) {
      logger.log(Level.WARNING, "Error to broadcast to all user");
      throw new RuntimeException(e);
    }
  }
  
  
  @Override
  protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws Exception {
    sessions.forEach((userId, ss) -> {
      try {
        ss.sendMessage(message);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
  
  public record WebSocketMessage(MessageTypes type, Object data) {}
}
