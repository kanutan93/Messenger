package ru.just.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.just.messenger.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);
}
