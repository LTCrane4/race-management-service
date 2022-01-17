package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.error.UserNotFoundException;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseService implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  @Override
  public User getUser(UUID id) throws UserNotFoundException {
    logger.trace("getUser called");

    Optional<User> user = this.userRepository.findById(id);

    // todo update this when user creation works
    return user.orElse(new User());
  }
}