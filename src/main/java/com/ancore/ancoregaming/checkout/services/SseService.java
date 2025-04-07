package com.ancore.ancoregaming.checkout.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {
  public final Map<String, Sinks.Many<Object>> clients = new ConcurrentHashMap<>();
  
  public Sinks.Many<Object> addClient(String userId) {
    Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();
    clients.put(userId, sink);
    return sink;
  }
  
  public void removeClient(String userId) {
    clients.remove(userId);
  }
  
  public void sendToClient(String userId, Object data) {
    if (clients.containsKey(userId)) {
      clients.get(userId).tryEmitNext(data);
    }
  }
}
