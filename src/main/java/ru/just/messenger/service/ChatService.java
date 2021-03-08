package ru.just.messenger.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.just.messenger.model.Chat;
import ru.just.messenger.model.User;
import ru.just.messenger.repository.ChatRepository;

/**
 * Chat service.
 */
@Service
public class ChatService {

  private final ChatRepository chatRepository;
  private final UserService userService;

  @Autowired
  public ChatService(ChatRepository chatRepository, UserService userService) {
    this.chatRepository = chatRepository;
    this.userService = userService;
  }

  /**
   * Get current user's chats.
   */
  public List<Chat> getChats() {
    User currentUser = userService.getCurrentUser();
    return chatRepository.getChatsByParticipant(currentUser);
  }

  /**
   * Save chat.
   */
  public Chat saveChat(Chat chat) {
    return chatRepository.save(chat);
  }
}
