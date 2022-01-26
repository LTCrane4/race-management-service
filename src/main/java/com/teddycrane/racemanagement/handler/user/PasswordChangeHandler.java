package com.teddycrane.racemanagement.handler.user;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.handler.user.request.ChangePasswordHandlerRequest;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.utils.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordChangeHandler implements Handler<ChangePasswordHandlerRequest, Boolean> {

  private final UserRepository userRepository;

  public PasswordChangeHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private String encodePassword(String rawPassword) {
    return PasswordEncoder.encodePassword(rawPassword);
  }

  @Override
  public Boolean resolve(ChangePasswordHandlerRequest input)
      throws BadRequestException, NotFoundException {
    User user =
        this.userRepository
            .findById(input.getId())
            .orElseThrow(() -> new NotFoundException("No user found for the provided id"));

    String encodedPassword = this.encodePassword(input.getOldPassword());
    if (encodedPassword.equals(user.getPassword())) {
      user.setPassword(this.encodePassword(input.getNewPassword()));
      this.userRepository.save(user);

      return true;
    } else {
      throw new BadRequestException("Previous password provided was incorrect.  Please try again.");
    }
  }
}
