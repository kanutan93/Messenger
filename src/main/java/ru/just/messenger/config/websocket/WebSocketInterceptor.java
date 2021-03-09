package ru.just.messenger.config.websocket;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;
import ru.just.messenger.config.security.SecurityConst;

@Configuration
public class WebSocketInterceptor implements HandshakeInterceptor {

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest req, ServerHttpResponse res,
      WebSocketHandler webSocketHandler, Map<String, Object> map
  )
      throws Exception {
    ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) req;
    ServletServerHttpResponse servletServerResponse = (ServletServerHttpResponse) res;
    HttpServletRequest request = servletServerRequest.getServletRequest();
    HttpServletResponse response = servletServerResponse.getServletResponse();

    Cookie cookie = WebUtils.getCookie(request, SecurityConst.AUTH_TOKEN);
    if (cookie != null) {
      String user =
          JWT.require(Algorithm.HMAC256(SecurityConst.SECRET)).build().verify(cookie.getValue()).getSubject();
      return user != null;
    }
    return false;
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
      WebSocketHandler webSocketHandler, Exception e
  ) {

  }
}
