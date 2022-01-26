package com.teddycrane.racemanagement.handler.user;

import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.handler.user.request.UpdateUserHandlerRequest;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserHandler implements Handler<UpdateUserHandlerRequest, User> {

  private final UserRepository userRepository;

  public UpdateUserHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User resolve(@NonNull UpdateUserHandlerRequest input) {
    Optional<User> existing = this.userRepository.findById(input.getRequesterId());

    if (existing.isPresent()) {
      User user = existing.get();
      if (input.getFirstName() != null) {
        user.setFirstName(input.getFirstName());
      }
      if (input.getLastName() != null) {
        user.setLastName(input.getLastName());
      }
      if (input.getEmail() != null) {
        user.setEmail(input.getEmail());
      }
      if (input.getUserType() != null) {
        user.setUserType(input.getUserType());
      }
      return this.userRepository.save(user);
    } else {
      throw new NotFoundException("No user found for the provided id");
    }
  }
}
