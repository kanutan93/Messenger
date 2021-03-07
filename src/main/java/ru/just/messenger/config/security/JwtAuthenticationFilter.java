package ru.just.messenger.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.just.messenger.model.User;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              user.getUsername(), user.getPassword(), new ArrayList<>()));
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {
    String jwtToken =
        JWT.create()
            .withSubject(
                ((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
                    .getUsername())
            .sign(Algorithm.HMAC256(SecurityConst.SECRET));

    Cookie cookie = new Cookie(SecurityConst.AUTH_TOKEN, jwtToken);
    cookie.setMaxAge(7 * 24 * 60 * 60);
    // cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");

    response.addCookie(cookie);
  }
}
