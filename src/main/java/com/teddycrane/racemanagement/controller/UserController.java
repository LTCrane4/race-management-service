package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.InsufficientPermissionsException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.model.user.response.UserResponse;
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
  public UserResponse getUser(@PathVariable String id)
      throws BadRequestException, NotFoundException {
    logger.info("getUser called");

    try {
      UUID userId = UUID.fromString(id);
      User user = this.userService.getUser(userId);
      return new UserResponse(user);
    } catch (IllegalArgumentException e) {
      logger.error("The id {} is not a valid user id", id);
      throw new BadRequestException("Invalid user id provided");
    }
  }

  @GetMapping("/user/search")
  public UserCollectionResponse searchUsers(
      @RequestParam("type") SearchType searchType, @RequestParam("value") String searchValue)
      throws BadRequestException {
    logger.info("searchUsers called");

    try {
      return new UserCollectionResponse(this.userService.searchUsers(searchType, searchValue));
    } catch (IllegalArgumentException e) {
      logger.error("Mismatch: Unable to map search type {}, to value", searchType);
      throw new BadRequestException("Unable to map the search type to the search value");
    }
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
      throws BadRequestException, InsufficientPermissionsException {
    logger.info("updateUser called");
    UserAuditData auditData = this.getUserAuditData();
    this.printAuditLog(auditData.getUserName(), auditData.getUserId(), auditData.getUserType());

    if (auditData.getUserType().equals(UserType.USER)) {
      logger.error("This user does not have the proper permissions to update other users");
      throw new InsufficientPermissionsException();
    }

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

  @DeleteMapping("/{id}")
  public User deleteUser(@PathVariable String id)
      throws BadRequestException, InsufficientPermissionsException, NotFoundException {
    logger.info("deleteUser called");

    UserAuditData data = this.getUserAuditData();

    // verify the user has a role allowed to delete
    if (data.getUserType().equals(UserType.USER)) {
      logger.error("The user has insufficient permissions to perform this action");
      throw new InsufficientPermissionsException();
    }

    try {
      UUID userId = UUID.fromString(id);

      if (userId.equals(data.getUserId())) {
        logger.error("Users are unable to self-delete");
        throw new BadRequestException("Cannot delete the requesting user");
      }

      return this.userService.deleteUser(userId);
    } catch (IllegalArgumentException e) {
      logger.error("Unable to parse the provided id {}", id);
      throw new BadRequestException("Unable to parse the provided id");
    }
  }
}
