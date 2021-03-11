package ru.just.messenger.utils;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Ws utils.
 */
public class WebSocketUtils {

  /**
   * Get username from ws headers.
   */
  public static String getUsernameFromSimpMessageHeaderAccessor(
      SimpMessageHeaderAccessor headerAccessor) {
    UsernamePasswordAuthenticationToken token =
        (UsernamePasswordAuthenticationToken) headerAccessor.getMessageHeaders().get("simpUser");
    return (String) token.getPrincipal();
  }
}
