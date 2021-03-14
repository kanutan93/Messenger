package ru.just.messenger.controller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import ru.just.messenger.model.Message;
import ru.just.messenger.model.User;
import ru.just.messenger.service.ChatService;

/**
 * Chat ws controller.
 */
@Controller
public class ChatWebSocketController {

  @PersistenceContext
  EntityManager entityManager;

  private final ChatService chatService;

  @Autowired
  public ChatWebSocketController(ChatService chatService) {
    this.chatService = chatService;
  }

  /**
   * Create message and send to /topic/chats/chat/:id and /topic/chats/:userId topics.
   */
  @MessageMapping("/chats/{id}")
  public void createMessage(@DestinationVariable Long id, Message message,
      SimpMessageHeaderAccessor headerAccessor) {
    message = chatService.createMessage(id, message, headerAccessor);
    this.chatService.sendMessage("/topic/chats/chat/" + id, message);
    List<User> participants = message.getChat().getParticipants();
    for (User user : participants) {
      this.chatService.sendMessage("/topic/chats/" + user.getId(), message);
    }
  }
}
