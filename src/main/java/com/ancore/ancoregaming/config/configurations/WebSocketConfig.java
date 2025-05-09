package com.ancore.ancoregaming.config.configurations;

import com.ancore.ancoregaming.websocket.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
  @Autowired
  private MessageService messageService;
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(messageService, "/ws")
        .setAllowedOrigins("*")
        .addInterceptors(new HttpSessionHandshakeInterceptor());
  }
}
