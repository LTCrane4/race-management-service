package com.teddycrane.racemanagement.handler.user;

import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.handler.user.request.DeleteUserRequest;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserHandler implements Handler<DeleteUserRequest, User> {

  private final UserRepository userRepository;

  public DeleteUserHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User resolve(@NonNull DeleteUserRequest input) {
    User u = this.userRepository.findById(input.getId()).orElse(null);

    if (u != null) {
      this.userRepository.delete(u);
      return u;
    } else {
      throw new NotFoundException("No user found to delete with the specified id");
    }
  }
}
