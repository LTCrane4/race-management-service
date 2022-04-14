package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.model.user.request.*;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
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
  ResponseEntity<UserResponse> getUser(@PathVariable("id") String id);

  // TODO update this to use a POST instead
  @GetMapping(value = "/user/search", produces = "application/json", consumes = "application/json")
  @Operation(summary = "Search users")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found users matching search params",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserCollectionResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search type or search type and search value mismatch")
      })
  ResponseEntity<UserCollectionResponse> searchUsers(
      @RequestParam("type") SearchType searchType, @RequestParam("value") String searchValue);

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
  ResponseEntity<? extends Response> createUser(@Valid @RequestBody CreateUserRequest request);

  @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
  ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request);

  @PatchMapping(value = "/user/{id}", produces = "application/json", consumes = "application/json")
  ResponseEntity<UserResponse> updateUser(
      @PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest request);

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
      @PathVariable("id") String id, @Valid @RequestBody ChangePasswordRequest request);

  @DeleteMapping(value = "/user/{id}", produces = "application/json", consumes = "application/json")
  ResponseEntity<UserResponse> deleteUser(@PathVariable("id") String id);

  @PutMapping(
      value = "/user/{id}/status",
      produces = "application/json",
      consumes = "application/json")
  ResponseEntity<? extends Response> changeStatus(
      @PathVariable("id") String id, @Valid @RequestBody ChangeStatusRequest request);
}
