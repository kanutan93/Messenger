package ru.just.messenger.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.just.messenger.model.Chat;
import ru.just.messenger.model.User;

/**
 * Chat repository.
 */
public interface ChatRepository extends JpaRepository<Chat, Long> {

  @Query("SELECT c FROM chats c WHERE :participant MEMBER OF c.participants")
  List<Chat> getChatsByParticipant(User participant);

  @Query("SELECT c FROM chats c WHERE c.id = :chatId")
  Chat findChatById(Long chatId);

  @Query("SELECT c.participants FROM chats c WHERE c.id = :chatId")
  List<User> findParticipantsByChatId(Long chatId);
}
