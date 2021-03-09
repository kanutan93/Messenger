package ru.just.messenger.controller;

import static ru.just.messenger.config.security.SecurityConst.IS_LOGGED_IN_URL;
import static ru.just.messenger.config.security.SecurityConst.SIGN_OUT_URL;
import static ru.just.messenger.config.security.SecurityConst.SIGN_UP_URL;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.just.messenger.model.User;
import ru.just.messenger.service.AuthService;

@RestController
public class AuthController {

  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Sign up.
   */
  @PostMapping(SIGN_UP_URL)
  public void signUp(@RequestBody User user) {
    if (!authService.createUser(user)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }

  @PostMapping(SIGN_OUT_URL)
  public void signOut(HttpServletResponse response) {
    Cookie cookieForDisableAuthToken = authService.getCookieForDisableAuthToken();
    response.addCookie(cookieForDisableAuthToken);
  }

  @GetMapping(IS_LOGGED_IN_URL)
  public boolean isLoggedIn() {
    return authService.isLoggedIn();
  }
}
