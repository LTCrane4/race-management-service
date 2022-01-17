package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.services.UserService;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    }
  }
}
