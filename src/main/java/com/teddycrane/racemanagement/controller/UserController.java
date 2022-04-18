package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.*;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.model.user.request.*;
import com.teddycrane.racemanagement.model.user.response.ChangePasswordResponse;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.model.user.response.UserResponse;
import com.teddycrane.racemanagement.services.UserService;
import com.teddycrane.racemanagement.utils.ResponseStatusGenerator;
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

  public ResponseEntity<UserCollectionResponse> getAllUsers() {
    logger.info("getAllUsers called");

    return ResponseEntity.ok(new UserCollectionResponse(this.userService.getAllUsers()));
  }

  public ResponseEntity<UserResponse> getUser(String id) {
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

  @Override
  public ResponseEntity<ErrorResponse> searchUsers(SearchUserRequest request) {
    logger.info("searchUsers (deprecated) called");
    logger.warn("Redirecting to new user search");
    return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
        .body(new ErrorResponse("Permanently moved to POST /users/search"));
  }

  public ResponseEntity<? extends Response> createUser(CreateUserRequest request) {
    logger.info("createUser called");

    try {
      return ResponseEntity.ok(new UserResponse(this.userService.createUser(request)));
    } catch (DuplicateItemException e) {
      logger.error("The username {} is already taken", request.getUsername());
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new ErrorResponse("The specified username is already taken"));
    }
  }

  public ResponseEntity<UserResponse> updateUser(String id, UpdateUserRequest request) {
    logger.info("updateUser called");
    UserAuditData auditData = this.getUserAuditData();
    this.printAuditLog(auditData.getUserName(), auditData.getUserId(), auditData.getUserType());

    if (auditData.getUserType().equals(UserType.USER)) {
      logger.error("This user does not have the proper permissions to update other users");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    try {
      UUID userId = UUID.fromString(id);
      Instant updated = null;

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
            new UserResponse(
                this.userService.updateUser(
                    userId,
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getUserType(),
                    updated)));
      } else {
        logger.error("At least one parameter must be supplied to update a User!");
        return ResponseEntity.badRequest().build();
      }
    } catch (IllegalArgumentException e) {
      logger.error(
          "Unable to parse one of the required values id: {}, updatedTimestamp: {}",
          id,
          request.getUpdatedTimestamp());
      return ResponseEntity.badRequest().build();
    } catch (ConflictException e) {
      logger.error("The timestamp provided is not the most recent");
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (NotFoundException e) {
      logger.error("No user found for the id {}", id);
      return ResponseEntity.notFound().build();
    } catch (InternalServerError e) {
      logger.error("An internal server error occurred");
      return ResponseEntity.internalServerError().build();
    }
  }

  @Override
  public ResponseEntity<? extends Response> changePassword(
      String id, @Valid ChangePasswordRequest request) {
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

        return this.createErrorResponse("Forbidden", HttpStatus.FORBIDDEN);
      }

      return ResponseEntity.ok(
          new ChangePasswordResponse(
              this.userService.changePassword(userId, oldPw, newPw), userId));
    } catch (IllegalArgumentException | BadRequestException e) {
      logger.error("Unable to parse the provided request");
      return this.createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (NotFoundException e) {
      logger.error("No user found for the id {}", id);
      return this.createErrorResponse(
          String.format("No user found for the id %s", id), HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<UserResponse> deleteUser(String id) throws NotFoundException {
    logger.info("deleteUser called");

    UserAuditData data = this.getUserAuditData();

    // verify the user has a role allowed to delete
    if (data.getUserType().equals(UserType.USER)) {
      logger.error("The user has insufficient permissions to perform this action");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    try {
      UUID userId = UUID.fromString(id);

      if (userId.equals(data.getUserId())) {
        logger.error("Users are unable to self-delete");
        return ResponseEntity.badRequest().build();
      }

      return ResponseEntity.ok(new UserResponse(this.userService.deleteUser(userId)));
    } catch (IllegalArgumentException e) {
      logger.error("Unable to parse the provided id {}", id);
      return ResponseEntity.badRequest().build();
    } catch (NotFoundException e) {
      logger.error("No user found for the id {}", id);
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  public ResponseEntity<? extends Response> changeStatus(String id, ChangeStatusRequest request) {
    logger.info("changeStatus called");

    try {
      UUID userId = UUID.fromString(id);
      Instant timestamp = Instant.parse(request.getUpdatedTimestamp());

      return ResponseEntity.ok(
          new UserResponse(this.userService.changeStatus(userId, request.getStatus(), timestamp)));
    } catch (IllegalArgumentException e) {
      logger.error("Bad Request: unable to parse the provided ID");
      return ResponseStatusGenerator.generateBadRequestResponse(
          "Unable to parse the provided user ID");
    } catch (DateTimeParseException e) {
      logger.error("Bad Request: invalid timestamp found");
      return ResponseStatusGenerator.generateBadRequestResponse("Invalid timestamp.");
    } catch (NotFoundException e) {
      logger.error("No user found for the id {}", id);
      return ResponseStatusGenerator.generateNotFoundResponse(
          String.format("No user found for the id %s", id));
    } catch (ConflictException e) {
      logger.error("Timestamp mismatch");
      return ResponseStatusGenerator.generateConflictResponse(
          "The updated timestamps do not match.  Re-fetch data and try again");
    }
  }
}
