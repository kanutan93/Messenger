package ru.just.messenger.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.just.messenger.model.User;
import ru.just.messenger.service.UserService;

/**
 * User controller.
 */
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/current")
  public User getCurrentUser() {
    return userService.getCurrentUser();
  }

  @PutMapping("/current/avatar")
  public void updateAvatar(@RequestParam("avatar") MultipartFile avatar) throws IOException {
    userService.updateAvatar(avatar);
  }

  @GetMapping
  public Collection<User> getUsers() {
    return userService.getUsers();
  }

  @GetMapping("/{username}")
  public User getUser(@PathVariable String username) {
    return userService.getCurrentUser();
  }

  /**
   * Get avatar input stream.
   */
  @GetMapping("/{username}/avatar")
  public ResponseEntity<InputStreamResource> getAvatar(@PathVariable String username)
      throws IOException {
    try {
      File file = new File(userService.getUser(username).getAvatarPath());

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + file.getName() + "\"")
          .contentLength(file.length())
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(new InputStreamResource(new FileInputStream(file)));
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
