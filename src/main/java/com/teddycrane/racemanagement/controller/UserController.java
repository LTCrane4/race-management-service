package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.*;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.model.user.UserDTO;
import com.teddycrane.racemanagement.model.user.request.*;
import com.teddycrane.racemanagement.model.user.response.ChangePasswordResponse;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.services.UserService;
import com.teddycrane.racemanagement.utils.mapper.UserMapper;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends BaseController implements UserApi {

  private final UserService userService;

  public UserController(UserService userService) {
    super();
    this.userService = userService;
  }

  @Override
  public ResponseEntity<UserCollectionResponse> getAllUsers() {
    logger.info("getAllUsers called");

    return ResponseEntity.ok(UserMapper.convertEntityListToDTO(this.userService.getAllUsers()));
  }

  @Override
  public ResponseEntity<UserDTO> getUser(String id) throws BadRequestException {
    logger.info("getUser called");

    try {
      UUID userId = UUID.fromString(id);
      return ResponseEntity.ok(UserMapper.convertEntityToDTO(this.userService.getUser(userId)));
    } catch (IllegalArgumentException e) {
      logger.error("The id {} is not a valid user id", id);
      throw new BadRequestException(String.format("The id %s is not a valid user id", id));
    } catch (NotFoundException e) {
      logger.error("No user found for the id {}", id);
      throw new NotFoundException(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<ErrorResponse> searchUsers(SearchUserRequest request) {
    logger.info("searchUsers (deprecated) called");
    logger.warn("Redirecting to new user search");
    return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
        .body(new ErrorResponse("Permanently moved to POST /users/search"));
  }

  @Override
  public ResponseEntity<UserDTO> createUser(@Valid CreateUserRequest request)
      throws DuplicateItemException {
    logger.info("createUser called");

    return ResponseEntity.ok(UserMapper.convertEntityToDTO(this.userService.createUser(request)));
  }

  @Override
  public ResponseEntity<UserDTO> updateUser(String id, @Valid UpdateUserRequest request)
      throws BadRequestException, ForbiddenException {
    logger.info("updateUser called");
    UserAuditData auditData = this.getUserAuditData();
    this.printAuditLog(auditData.getUserName(), auditData.getUserId(), auditData.getUserType());

    if (auditData.getUserType().equals(UserType.USER)) {
      logger.error(
          "{} does not have the proper permissions to update other users", auditData.getUserName());
      throw new ForbiddenException();
    }

    try {
      UUID userId = UUID.fromString(id);
      Instant updated;

      if (request.getUpdatedTimestamp() != null) {
        updated = Instant.parse(request.getUpdatedTimestamp());
      } else {
        throw new IllegalArgumentException("The provided instant was not valid");
      }

      // validate that at least one of the request body parameters are not null
      if (request.getFirstName() != null
          || request.getLastName() != null
          || request.getEmail() != null
          || request.getUserType() != null) {
        return ResponseEntity.ok(
            UserMapper.convertEntityToDTO(
                this.userService.updateUser(
                    userId,
                    updated,
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getUserType())));
      } else {
        logger.error("At least one parameter must be supplied to update a User!");
        throw new BadRequestException("At least one parameter must be provided to update a User!");
      }
    } catch (IllegalArgumentException e) {
      logger.error(
          "Unable to parse one of the required values id: {}, updatedTimestamp: {}",
          id,
          request.getUpdatedTimestamp());
      throw new BadRequestException("Unable to parse a required value");
    }
  }

  @Override
  public ResponseEntity<ChangePasswordResponse> changePassword(
      String id, @Valid ChangePasswordRequest request)
      throws BadRequestException, ForbiddenException {
    logger.info("changePassword called");
    UserAuditData audit = this.getUserAuditData();
    String oldPw, newPw;

    try {
      UUID userId = UUID.fromString(id);
      oldPw = new String(Base64.getDecoder().decode(request.getOldPassword()));
      newPw = new String(Base64.getDecoder().decode(request.getNewPassword()));

      logger.info("Password change requested for {}", userId);

      if (!audit.getUserId().equals(userId)) {
        logger.error(
            "User {} does not have the permissions to change another user's password",
            audit.getUserId());

        throw new ForbiddenException();
      }

      return ResponseEntity.ok(
          new ChangePasswordResponse(
              this.userService.changePassword(userId, oldPw, newPw), userId));
    } catch (IllegalArgumentException | BadRequestException e) {
      logger.error("Unable to parse the provided request");
      throw new BadRequestException("Unable to parse the request");
    }
  }

  @Override
  public ResponseEntity<UserDTO> deleteUser(String id)
      throws BadRequestException, NotFoundException, ForbiddenException,
          TransitionNotAllowedException {
    logger.info("deleteUser called");

    UserAuditData data = this.getUserAuditData();

    // verify the user has a role allowed to delete
    if (data.getUserType().equals(UserType.USER)) {
      throw new ForbiddenException("Insufficient Permissions");
    }

    try {
      UUID userId = UUID.fromString(id);

      if (userId.equals(data.getUserId())) {
        logger.error("Users are unable to self-delete");
        throw new TransitionNotAllowedException("A user cannot delete themselves");
      }

      return ResponseEntity.ok(UserMapper.convertEntityToDTO(this.userService.deleteUser(userId)));
    } catch (IllegalArgumentException e) {
      logger.error("Unable to parse the provided id {}", id);
      throw new BadRequestException("Unable to parse the provided id");
    }
  }

  @Override
  public ResponseEntity<UserDTO> changeStatus(String id, ChangeStatusRequest request) {
    logger.info("changeStatus called");

    try {
      UUID userId = UUID.fromString(id);
      Instant timestamp = Instant.parse(request.getUpdatedTimestamp());

      return ResponseEntity.ok(
          UserMapper.convertEntityToDTO(
              this.userService.changeStatus(userId, request.getStatus(), timestamp)));
    } catch (IllegalArgumentException | DateTimeParseException e) {
      throw new BadRequestException("Unable to parse the provided ID or timestamp");
    }
  }
}
