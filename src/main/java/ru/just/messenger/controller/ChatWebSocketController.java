package ru.just.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.just.messenger.model.Message;
import ru.just.messenger.service.ChatService;

@Controller
public class ChatWebSocketController {

  private final ChatService chatService;

  @Autowired
  public ChatWebSocketController(ChatService chatService) {
    this.chatService = chatService;
  }

  @MessageMapping("/chats/{id}")
  @SendTo("/topic/chats/{id}")
  public Message createMessage(@DestinationVariable Long id, Message message) {
    return chatService.createMessage(id, message);
  }
}
