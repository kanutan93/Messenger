package ru.just.messenger.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.just.messenger.model.Chat;
import ru.just.messenger.model.Message;
import ru.just.messenger.model.User;
import ru.just.messenger.repository.ChatRepository;
import ru.just.messenger.repository.MessageRepository;

/**
 * Chat service.
 */
@Service
public class ChatService {

  private final ChatRepository chatRepository;
  private final UserService userService;
  private final MessageRepository messageRepository;

  /**
   * Constructor.
   */
  @Autowired
  public ChatService(ChatRepository chatRepository, UserService userService,
      MessageRepository messageRepository) {
    this.chatRepository = chatRepository;
    this.userService = userService;
    this.messageRepository = messageRepository;
  }

  /**
   * Get current user's chats.
   */
  public List<Chat> getChats() {
    User currentUser = userService.getCurrentUser();
    return chatRepository.getChatsByParticipant(currentUser);
  }

  /**
   * Create chat.
   */
  public Chat createChat(Chat chat) {
    return chatRepository.save(chat);
  }

  /**
   * Create message.
   */
  public Message createMessage(Long chatId, Message message) {
    Chat chat = chatRepository.getOne(chatId);
    User user = userService.getCurrentUser();
    message.setWho(user.getId());
    message.setChat(chat);
    message.setTime(new Date().toGMTString());
    return messageRepository.save(message);
  }
}
