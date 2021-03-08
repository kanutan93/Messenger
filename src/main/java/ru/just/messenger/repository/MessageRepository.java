package ru.just.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.just.messenger.model.Message;

/**
 * Message repository.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

}
