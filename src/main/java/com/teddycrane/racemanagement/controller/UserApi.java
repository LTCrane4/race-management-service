package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.ForbiddenException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.error.TransitionNotAllowedException;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.model.user.request.*;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.model.user.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public interface UserApi {

  @GetMapping(value = "/user", produces = "application/json", consumes = "application/json")
  @Operation(summary = "Get all Users")
  @ApiResponse(
      responseCode = "200",
      description = "Found users",
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserCollectionResponse.class))
      })
  ResponseEntity<UserCollectionResponse> getAllUsers();

  @GetMapping(value = "/user/{id}", produces = "application/json", consumes = "application/json")
  @Operation(summary = "Get single user by id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found user",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class))
            }),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Bad request - Invalid id")
      })
  ResponseEntity<UserResponse> getUser(@PathVariable("id") String id) throws BadRequestException;

  @PostMapping(value = "/user/search", produces = "application/json", consumes = "application/json")
  @Operation(summary = "Search users (deprecated)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "301",
            description = "Permanently moved to /users/search",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
            })
      })
  @Deprecated
  ResponseEntity<ErrorResponse> searchUsers(@Valid @RequestBody SearchUserRequest request);

  @PostMapping(value = "/user/new", consumes = "application/json", produces = "application/json")
  @Operation(summary = "Create new User")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Successfully created user",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body format/request body missing required parameters",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "409",
            description =
                "Failed to create - A user with the provided username/email already exists",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
      })
  ResponseEntity<? extends Response> createUser(@Valid @RequestBody CreateUserRequest request)
      throws DuplicateItemException;

  @PatchMapping(value = "/user/{id}", produces = "application/json", consumes = "application/json")
  ResponseEntity<UserResponse> updateUser(
      @PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest request)
      throws BadRequestException, ForbiddenException;

  @PatchMapping(
      value = "/user/{id}/change-password",
      produces = "application/json",
      consumes = "application/json")
  @Operation(description = "Change password")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully changed password",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class))
            })
      })
  ResponseEntity<? extends Response> changePassword(
      @PathVariable("id") String id, @Valid @RequestBody ChangePasswordRequest request)
      throws BadRequestException, ForbiddenException;

  @DeleteMapping(value = "/user/{id}", produces = "application/json", consumes = "application/json")
  ResponseEntity<UserResponse> deleteUser(@PathVariable("id") String id)
      throws BadRequestException, NotFoundException, ForbiddenException,
          TransitionNotAllowedException;

  @PutMapping(
      value = "/user/{id}/status",
      produces = "application/json",
      consumes = "application/json")
  @Operation(description = "Change user status")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully changed user status",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID format or invalid timestamp",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
      })
  ResponseEntity<? extends Response> changeStatus(
      @PathVariable("id") String id, @Valid @RequestBody ChangeStatusRequest request);
}
