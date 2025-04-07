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
  private final Map<String, FluxSink<Object>> clients = new ConcurrentHashMap<>();
  
  public Flux<ServerSentEvent<Object>>addClient(String userId) {
    return Flux.create(emitter -> {
      clients.put(userId, emitter);
      emitter.onDispose(() -> clients.remove(userId));
    }).map(data -> ServerSentEvent.builder()
        .event("payment")
        .data(data)
        .build());
  }
  
  public void removeClient(String userId) {
    clients.remove(userId);
  }
  
  public void sendToClient(String userId, Object data) {
    if (clients.containsKey(userId)) {
      clients.get(userId).next(data);
    }
  }
}
