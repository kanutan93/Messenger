package ru.just.messenger.service;

import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.just.messenger.config.security.SecurityConst;
import ru.just.messenger.model.User;
import ru.just.messenger.repository.UserRepository;

@Service
public class AuthService {

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Create user.
   */
  public boolean createUser(User user) {
    User userWithSameName = userRepository.findByUsername(user.getUsername());
    if (userWithSameName == null) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
      return true;
    }
    return false;
  }

  /**
   * Set auth token cookie to null and set cookie's age to 0 to disable it.
   */
  public Cookie getCookieForDisableAuthToken() {
    Cookie cookie = new Cookie(SecurityConst.AUTH_TOKEN, null);
    cookie.setMaxAge(0);
    return cookie;
  }

  /**
   * Is user logged in.
   */
  public boolean isLoggedIn() {
    return SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        && !(SecurityContextHolder.getContext().getAuthentication()
        instanceof AnonymousAuthenticationToken);
  }
}
