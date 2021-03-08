package ru.just.messenger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.lang.NonNull;

/**
 * Message entity.
 */
@Entity(name = "messages")
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NonNull
  private Long who;

  private String message;

  @NonNull
  private String time;

  @ManyToOne
  @JoinColumn(name = "chat_id", nullable = false)
  @JsonIgnore
  private Chat chat;

  @NonNull
  public Long getWho() {
    return who;
  }

  public void setWho(@NonNull Long who) {
    this.who = who;
  }

  @NonNull
  public String getMessage() {
    return message;
  }

  public void setMessage(@NonNull String message) {
    this.message = message;
  }

  @NonNull
  public String getTime() {
    return time;
  }

  public void setTime(@NonNull String time) {
    this.time = time;
  }

  public Chat getChat() {
    return chat;
  }

  public void setChat(Chat chat) {
    this.chat = chat;
  }
}
