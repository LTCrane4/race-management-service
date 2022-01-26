package com.teddycrane.racemanagement.handler.user;

import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class GetUserHandler implements Handler<UUID, User> {

  private final UserRepository userRepository;

  public GetUserHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Resolves a user based on their UUID.
   *
   * @param input The UUID for the user to find.
   * @return The User associated with the UUID, or null if no user is found.
   */
  @Override
  public User resolve(UUID input) {
    return this.userRepository.findById(input).orElse(null);
  }
}
