package com.teddycrane.racemanagement.handler.user;

import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
public class GetUsersHandler implements Handler<String, Collection<User>> {

  private final UserRepository userRepository;

  public GetUsersHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Returns a list of all users.
   *
   * @param input Placeholder for future use.
   * @return A Collection of all users.
   */
  @Override
  public Collection<User> resolve(String input) {
    return this.userRepository.findAll();
  }
}
