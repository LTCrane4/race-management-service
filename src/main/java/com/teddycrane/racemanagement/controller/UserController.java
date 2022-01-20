package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.util.*;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends BaseController {

  private final UserService userService;

  public UserController(UserService userService) {
    super();
    this.userService = userService;
  }

  @GetMapping("/user")
  public UserCollectionResponse getAllUsers() {
    logger.info("getAllUsers called");

    return new UserCollectionResponse(this.userService.getAllUsers());
  }

  @GetMapping("/user/{id}")
  public User getUser(@PathVariable String id) throws BadRequestException, NotFoundException {
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

  @GetMapping("/user/search")
  public UserCollectionResponse searchUsers(
      @RequestParam("type") SearchType searchType, @RequestParam("value") String searchValue) {
    logger.info("searchUsers called");

    return new UserCollectionResponse(this.userService.searchUsers(searchType, searchValue));
  }

  @PostMapping("/user/new")
  public User createUser(@Valid @RequestBody CreateUserRequest request) {
    logger.info("createUser called");

    return this.userService.createUser(
        request.getUsername(),
        request.getPassword(),
        request.getFirstName(),
        request.getLastName(),
        request.getEmail(),
        request.getUserType());
  }

  @PostMapping("/login")
  public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request)
      throws NotAuthorizedException {
    logger.info("login requested");
    return this.userService.login(request.getUsername(), request.getPassword());
  }

  @PatchMapping("/user/{id}")
  public User updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserRequest request)
      throws BadRequestException {
    logger.info("updateUser called");

    try {
      UUID userId = UUID.fromString(id);
      // validate that at least one of the request body parameters are not null
      if (request.getFirstName() != null
          || request.getLastName() != null
          || request.getEmail() != null
          || request.getUserType() != null) {
        return this.userService.updateUser(
            userId,
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getUserType());
      } else {
        logger.error("At least one parameter must be supplied to update a User!");
        throw new BadRequestException("Not enough parameters supplied to update a user");
      }
    } catch (IllegalArgumentException e) {
      logger.error("Unable to parse the provided id {}", id);
      throw new BadRequestException("Unable to parse the provided id");
    }
  }
}
