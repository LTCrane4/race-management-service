package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.InsufficientPermissionsException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.user.request.ChangePasswordRequest;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.model.user.response.ChangePasswordResponse;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.model.user.response.UserResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.util.*;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends BaseController {

  private final UserService userService;

  public UserController(UserService userService) {
    super();
    this.userService = userService;
  }

  @GetMapping("/user")
  public ResponseEntity<UserCollectionResponse> getAllUsers() {
    logger.info("getAllUsers called");

    return ResponseEntity.ok(new UserCollectionResponse(this.userService.getAllUsers()));
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
    logger.info("getUser called");

    try {
      UUID userId = UUID.fromString(id);
      return ResponseEntity.ok(new UserResponse(this.userService.getUser(userId)));
    } catch (IllegalArgumentException e) {
      logger.error("The id {} is not a valid user id", id);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (NotFoundException e) {
      logger.error("No user found for the id {}", id);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
  public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
    logger.info("createUser called");

    return new UserResponse(this.userService.createUser(request));
  }

  @PostMapping("/login")
  public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request)
      throws NotAuthorizedException {
    logger.info("login requested");
    return this.userService.login(request.getUsername(), request.getPassword());
  }

  @PatchMapping("/user/{id}")
  public UserResponse updateUser(
      @PathVariable String id, @Valid @RequestBody UpdateUserRequest request)
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
        return new UserResponse(this.userService.updateUser(userId, request));
      } else {
        logger.error("At least one parameter must be supplied to update a User!");
        throw new BadRequestException("Not enough parameters supplied to update a user");
      }
    } catch (IllegalArgumentException e) {
      logger.error("Unable to parse the provided id {}", id);
      throw new BadRequestException("Unable to parse the provided id");
    }
  }

  @PatchMapping("/{id}/change-password")
  public ChangePasswordResponse changePassword(
      @Valid @RequestBody ChangePasswordRequest request, @PathVariable String id)
      throws NotFoundException, BadRequestException, InsufficientPermissionsException {
    logger.info("changePassword called");
    UserAuditData audit = this.getUserAuditData();

    try {
      UUID userId = UUID.fromString(id);
      logger.info("Password change requested for {}", userId);

      if (!audit.getUserId().equals(userId)) {
        logger.error(
            "User {} does not have the permissions to change another user's password",
            audit.getUserId());
        throw new InsufficientPermissionsException();
      }

      return new ChangePasswordResponse(
          this.userService.changePassword(
              userId, request.getOldPassword(), request.getNewPassword()),
          userId);
    } catch (IllegalArgumentException e) {
      logger.error("Unable to parse the provided id");
      throw new BadRequestException("Unable to parse the provided id!");
    }
  }

  @DeleteMapping("/user/{id}")
  public UserResponse deleteUser(@PathVariable String id)
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

      return new UserResponse(this.userService.deleteUser(userId));
    } catch (IllegalArgumentException e) {
      logger.error("Unable to parse the provided id {}", id);
      throw new BadRequestException("Unable to parse the provided id");
    }
  }
}
