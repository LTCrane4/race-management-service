package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
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
  public User getUser(UUID id) throws NotFoundException {
    logger.trace("getUser called");

    Optional<User> user = this.userRepository.findById(id);

    if (user.isPresent()) {
      return user.get();
    } else {
      logger.error("No user found for the id {}", id);
      throw new NotFoundException("No user found for the provided id");
    }
  }

  @Override
  public User createUser(String username, String password, String firstName,
                         String lastName, String email, UserType userType)
      throws DuplicateItemException {
    Optional<User> existing = this.userRepository.findByUsername(username);

    if (existing.isPresent()) {
      logger.error("A user with the same username already exists! ");
      throw new DuplicateItemException(
          "This username is already taken.  Please try a different username");
    }

    return this.userRepository.save(
        new User(firstName, lastName, username, email, password, userType));
  }
}
