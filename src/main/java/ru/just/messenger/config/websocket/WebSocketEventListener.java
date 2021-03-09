package ru.just.messenger.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.just.messenger.model.User;
import ru.just.messenger.service.ChatService;
import ru.just.messenger.service.UserService;

@Configuration
public class WebSocketEventListener {

  private final UserService userService;

  @Autowired
  public WebSocketEventListener(UserService userService) {
    this.userService = userService;
  }

  @EventListener
  private void handleSessionConnected(SessionConnectEvent event) {
    String username = getUsername(event);
    User user = userService.getUser(username);
    ChatService.USERS_ONLINE.put(getSessionId(event), user.getId());
  }

  @EventListener
  private void handleSessionDisconnect(SessionDisconnectEvent event) {
    ChatService.USERS_ONLINE.remove(getSessionId(event));
  }

  private String getSessionId(AbstractSubProtocolEvent event) {
    SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
    return headers.getSessionId();
  }

  private String getUsername(AbstractSubProtocolEvent event) {
    SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
    UsernamePasswordAuthenticationToken token =
        (UsernamePasswordAuthenticationToken) headers.getMessageHeaders().get("simpUser");
    return (String) token.getPrincipal();
  }
}
