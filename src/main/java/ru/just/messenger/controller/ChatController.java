package ru.just.messenger.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.just.messenger.model.Chat;
import ru.just.messenger.model.Message;
import ru.just.messenger.service.ChatService;

/**
 * Chat controller.
 */
@RestController
@RequestMapping("/chats")
public class ChatController {

  private final ChatService chatService;

  @Autowired
  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @GetMapping
  public List<Chat> getChats() {
    return chatService.getChats();
  }

  @PostMapping
  public Chat createChat(@RequestBody Chat chat) {
    return chatService.createChat(chat);
  }

  @PostMapping("/{id}")
  public Message createMessage(@PathVariable Long id, @RequestBody Message message) {
    return chatService.createMessage(id, message);
  }
}
