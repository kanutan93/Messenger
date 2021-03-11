package ru.just.messenger.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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
  public List<User> getUsers() {
    return userRepository.findAllWithout(getCurrentUser().getUsername())
        .stream().map(this::setUserStatus).collect(Collectors.toList());
  }

  /**
   * Get user.
   */
  public User getUser(String username) {
    User user = userRepository.findByUsername(username);
    user = setUserStatus(user);
    return user;
  }

  /**
   * Get current user from security context.
   */
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      String currentUserName = authentication.getName();
      User user = userRepository.findByUsername(currentUserName);
      user = setUserStatus(user);
      return user;
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

  /**
   * Set user status.
   */
  public User setUserStatus(User user) {
    boolean isUserOnline = ChatService.USERS_ONLINE.containsValue(user.getId());
    if (isUserOnline) {
      user.setStatus("online");
    } else {
      user.setStatus("offline");
    }
    return user;
  }
}
