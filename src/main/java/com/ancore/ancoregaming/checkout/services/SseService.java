package com.ancore.ancoregaming.checkout.services;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {
  private final Map<String, FluxSink<ServerSentEvent<?>>> clients = new ConcurrentHashMap<>();
  
  public enum EVENT_TYPES {
    PAYMENT,
    NEW_PAYMENT_RECEIVED
  }
  
  public Flux<ServerSentEvent<?>> addClient(String userId) {
    return Flux.create(emitter -> {
      clients.put(userId, emitter);
      emitter.onDispose(() -> clients.remove(userId));
    });
  }
  
  public void removeClient(String userId) {
    clients.remove(userId);
  }
  
  public void sendToClient(String userId, EVENT_TYPES eventName, Object data) {
    if (clients.containsKey(userId)) {
      ServerSentEvent<Object> event = ServerSentEvent.builder()
          .event(eventName.name())
          .data(data)
          .build();
      
      clients.get(userId).next(event);
    }
  }
}
