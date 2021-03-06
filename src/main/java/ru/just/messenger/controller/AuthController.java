package ru.just.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.just.messenger.config.security.SecurityConst;
import ru.just.messenger.jpa.user.User;
import ru.just.messenger.jpa.user.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping(SecurityConst.SIGN_UP_URL)
  public void signUp(@RequestBody User user) {
    User userWithSameName = userRepository.findByUsername(user.getUsername());
    if (userWithSameName == null) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
    } else {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }

  @PostMapping(SecurityConst.SIGN_OUT_URL)
  public void signOut(HttpServletResponse response) {
    Cookie cookie = new Cookie(SecurityConst.AUTH_TOKEN, null);
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

  @GetMapping(SecurityConst.IS_LOGGED_IN_URL)
  public boolean isLoggedIn() {
    return SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        && !(SecurityContextHolder.getContext().getAuthentication()
            instanceof AnonymousAuthenticationToken);
  }
}
