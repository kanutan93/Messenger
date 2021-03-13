package ru.just.messenger.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.just.messenger.model.User;
import ru.just.messenger.service.ChatService;
import ru.just.messenger.service.UserService;
import ru.just.messenger.utils.WebSocketUtils;

/**
 * Ws events listener.
 */
@Configuration
public class WebSocketEventListener {

  private static final String TOPIC_USERS = "/topic/users";

  private final UserService userService;
  private final ChatService chatService;

  @Autowired
  public WebSocketEventListener(UserService userService, ChatService chatService) {
    this.userService = userService;
    this.chatService = chatService;
  }

  @EventListener
  private void handleSessionConnected(SessionConnectEvent event) {
    String username = getUsername(event);
    User user = userService.getUser(username);
    ChatService.USERS_ONLINE.put(getSessionId(event), user.getId());
    chatService.sendMessage(TOPIC_USERS, ChatService.USERS_ONLINE.values());
  }

  @EventListener
  private void handleSessionDisconnect(SessionDisconnectEvent event) {
    ChatService.USERS_ONLINE.remove(getSessionId(event));
    chatService.sendMessage(TOPIC_USERS, ChatService.USERS_ONLINE.values());
  }

  private String getSessionId(AbstractSubProtocolEvent event) {
    SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
    return headers.getSessionId();
  }

  private String getUsername(AbstractSubProtocolEvent event) {
    SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
    return WebSocketUtils.getUsernameFromSimpMessageHeaderAccessor(headers);
  }
}
