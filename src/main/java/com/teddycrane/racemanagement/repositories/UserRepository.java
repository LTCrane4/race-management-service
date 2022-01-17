package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);

  /**
   * This method is required for Spring Security.
   * @param username The username to find by.
   * @return The user found for the username, or null.
   */
  User findOneByUsername(String username);
}
