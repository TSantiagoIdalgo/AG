package com.ancore.ancoregaming.checkout.services;

import com.ancore.ancoregaming.user.model.Role;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {
  @Autowired
  private IUserRepository userRepository;
  private final Map<String, Pair<User, Sinks.Many<ServerSentEvent<Object>>>> clients = new ConcurrentHashMap<>();
  
  public enum EVENT_TYPES {
    PAYMENT,
    NEW_PAYMENT_RECEIVED
  }
  
  public Flux<ServerSentEvent<Object>> addClient(User user) {
    Sinks.Many<ServerSentEvent<Object>> sink = Sinks.many().multicast().onBackpressureBuffer();
    clients.put(user.getEmail(), Pair.of(user, sink));
    
    return sink.asFlux()
        .doOnCancel(() -> {
          System.out.println("CLIENT DISCONNECTED: " + user.getEmail());
          clients.remove(user.getEmail());
        });
    
    
  }
  
  public void sendToClient(User user, EVENT_TYPES eventName, Object data) {
    if (clients.containsKey(user.getEmail())) {
      ServerSentEvent<Object> event = ServerSentEvent.builder()
          .event(eventName.name())
          .data(data)
          .build();
      Sinks.Many<ServerSentEvent<Object>> sink = clients.get(user.getEmail()).getSecond();
      sink.tryEmitNext(event);
    }
  }
  
  public void broadcast(EVENT_TYPES eventName, Object data) {
    ServerSentEvent<Object> event = ServerSentEvent.builder()
        .event(eventName.name())
        .data(data)
        .build();
    System.out.println(eventName);
    clients.forEach((email, pair) -> {
      User user = pair.getFirst();
      Sinks.Many<ServerSentEvent<Object>> sink = pair.getSecond();
      
      boolean isAdmin = user.getRoles().stream()
          .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
      
      if (isAdmin) {
        try {
          sink.tryEmitNext(event);
        } catch (Exception e) {
          System.out.println("ERROR SENDING TO " + email + ": " + e.getMessage());
        }
      }
    });
  }
  
  
}
