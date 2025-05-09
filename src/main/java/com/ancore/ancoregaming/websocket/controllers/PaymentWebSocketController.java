package com.ancore.ancoregaming.websocket.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentWebSocketController {
  
  @GetMapping("/home")
  public String showChatPage() {
    return "Chat";
  }
}
