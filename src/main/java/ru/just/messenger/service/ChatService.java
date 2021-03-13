package ru.just.messenger.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.just.messenger.model.Chat;
import ru.just.messenger.model.Message;
import ru.just.messenger.model.User;
import ru.just.messenger.repository.ChatRepository;
import ru.just.messenger.repository.MessageRepository;
import ru.just.messenger.utils.WebSocketUtils;

/**
 * Chat service.
 */
@Service
public class ChatService {

  public static final Map<String, Long> USERS_ONLINE = new HashMap<>();

  private final ChatRepository chatRepository;
  private final UserService userService;
  private final MessageRepository messageRepository;
  private final SimpMessagingTemplate template;

  /**
   * Constructor.
   */
  @Autowired
  public ChatService(ChatRepository chatRepository, UserService userService,
      MessageRepository messageRepository, SimpMessagingTemplate template) {
    this.chatRepository = chatRepository;
    this.userService = userService;
    this.messageRepository = messageRepository;
    this.template = template;
  }

  /**
   * Get current user's chats.
   */
  public List<Chat> getChats() {
    User currentUser = userService.getCurrentUser();
    return getChats(currentUser);
  }

  /**
   * Get user's chats.
   */
  public List<Chat> getChats(User user) {
    return chatRepository.getChatsByParticipant(user);
  }

  /**
   * Create chat.
   */
  public Chat createChat(Chat chat) {
    chat = chatRepository.save(chat);
    return chat;
  }

  /**
   * Create message.
   */
  public Message createMessage(Long chatId, Message message,
      SimpMessageHeaderAccessor headerAccessor) {
    Chat chat = chatRepository.getOne(chatId);
    User user = userService
        .getUser(WebSocketUtils.getUsernameFromSimpMessageHeaderAccessor(headerAccessor));
    message.setWho(user.getId());
    message.setChat(chat);
    message.setTime(new Date().toGMTString());
    return messageRepository.save(message);
  }

  /**
   * Send websocket message.
   */
  public void sendMessage(String destination, Object payload) {
    this.template.convertAndSend(destination, payload);
  }
}
