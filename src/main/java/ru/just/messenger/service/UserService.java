package ru.just.messenger.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.just.messenger.model.User;
import ru.just.messenger.repository.UserRepository;

/**
 * User service.
 */
@Service
public class UserService {

  @Value("${avatars.path}")
  private String avatarsPath;

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Get users.
   */
  public Collection<User> getUsers() {
    return userRepository.findAllWithout(getCurrentUser().getUsername());
  }

  public User getUser(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * Get current user from security context.
   */
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      String currentUserName = authentication.getName();
      return userRepository.findByUsername(currentUserName);
    }
    return null;
  }

  /**
   * Update user avatar.
   */
  public void updateAvatar(MultipartFile avatar) throws IOException {
    byte[] bytes = avatar.getBytes();
    File file = new File(avatarsPath + "/" + avatar.getOriginalFilename());
    if (!file.exists()) {
      BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
      stream.write(bytes);
      stream.close();
    }
    User currentUser = getCurrentUser();
    currentUser.setAvatarPath(file.getAbsolutePath());
    userRepository.save(currentUser);
  }
}
