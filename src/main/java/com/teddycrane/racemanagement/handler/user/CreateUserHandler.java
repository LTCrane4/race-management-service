package com.teddycrane.racemanagement.handler.user;

import static com.teddycrane.racemanagement.utils.PasswordEncoder.encodePassword;

import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateUserHandler implements Handler<CreateUserRequest, User> {

  private final UserRepository userRepository;

  public CreateUserHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User resolve(@NonNull @Valid CreateUserRequest input) {
    Optional<User> existing = this.userRepository.findByUsername(input.getUsername());

    if (existing.isPresent()) {
      throw new DuplicateItemException(
          "This username is already taken.  Please try a different username");
    }

    return this.userRepository.save(
        new User(
            input.getFirstName(),
            input.getLastName(),
            input.getUsername(),
            input.getEmail(),
            encodePassword(input.getPassword()),
            input.getUserType()));
  }
}
