package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.model.request.CreateUserRequest;
import com.teddycrane.racemanagement.services.UserService;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/user")
@RestController
public class UserController extends BaseController {
  private final UserService userService;

  public UserController(UserService userService) {
    super();
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public User getUser(@PathVariable String id) throws BadRequestException {
    logger.trace("getUser called");

    try {
      UUID userId = UUID.fromString(id);
      return this.userService.getUser(userId);
    } catch (IllegalArgumentException e) {
      logger.error("The id {} is not a valid user id");
      throw new BadRequestException("Invalid user id provided");
    } catch (NotFoundException e) {
      // pass error up chain
      throw new NotFoundException(e.getMessage());
    }
  }

  @PostMapping("/new")
  public User createUser(@Valid @RequestBody CreateUserRequest request) {
    logger.trace("createUser called");

    return this.userService.createUser(
        request.getUsername(), request.getPassword(), request.getFirstName(),
        request.getLastName(), request.getEmail(), request.getUserType());
  }
}
