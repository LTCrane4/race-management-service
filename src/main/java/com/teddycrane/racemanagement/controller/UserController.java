package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.model.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.response.AuthenticationResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends BaseController {

  private final UserService userService;

  public UserController(UserService userService) {
    super();
    this.userService = userService;
  }

  @GetMapping("/user/{id}")
  public User getUser(@PathVariable String id)
      throws BadRequestException, NotFoundException {
    logger.info("getUser called");

    try {
      UUID userId = UUID.fromString(id);
      Optional<User> _user = this.userService.getUser(userId);
      if (_user.isPresent()) {
        return _user.get();
      } else {
        logger.error("No user found for the id {}", id);
        throw new NotFoundException("No user found for the provided id");
      }
    } catch (IllegalArgumentException e) {
      logger.error("The id {} is not a valid user id", id);
      throw new BadRequestException("Invalid user id provided");
    } catch (NotFoundException e) {
      // pass error up chain
      throw new NotFoundException(e.getMessage());
    }
  }

  @PostMapping("/user/new")
  public User createUser(@Valid @RequestBody CreateUserRequest request) {
    logger.info("createUser called");

    return this.userService.createUser(
        request.getUsername(), request.getPassword(), request.getFirstName(),
        request.getLastName(), request.getEmail(), request.getUserType());
  }

  @PostMapping("/login")
  public AuthenticationResponse
  login(@RequestBody @Valid AuthenticationRequest request) {
    logger.info("login requested");

    try {
      return this.userService.login(request.getUsername(),
                                    request.getPassword());
    } catch (Exception e) {
      logger.error("an exception occurred");
      return new AuthenticationResponse("");
    }
  }
}
