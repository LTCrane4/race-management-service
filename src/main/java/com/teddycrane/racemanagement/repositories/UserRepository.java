package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.user.User;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * Finds a user by username. Returns an optional instead of null if the user is not found.
   *
   * @param username The username to find the User by.
   * @return An Optional wrapping the User found, or an empty Optional.
   */
  Optional<User> findByUsername(String username);

  /**
   * This method is required for Spring Security. Finds a user by username.
   *
   * @param username The username to find by.
   * @return The user found for the username, or null.
   */
  User findOneByUsername(String username);

  /**
   * Finds all users by UserType. The return value is never null.
   *
   * @param userType The UserType to find Users by.
   * @return A collection of all users with the supplied UserType. If no users are found with the
   *     specified user type, then an empty Collection is returned.
   */
  Collection<User> findAllByUserType(UserType userType);

  Collection<User> findAllByLastName(String lastName);
}
