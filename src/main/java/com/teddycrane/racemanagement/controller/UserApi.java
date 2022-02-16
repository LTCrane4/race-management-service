package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.user.request.ChangePasswordRequest;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface UserApi {

  @GetMapping("/user")
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

  @GetMapping("/user/{id}")
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

  @GetMapping("/user/search")
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

  @PostMapping("/user/new")
  @Operation(summary = "Create new User")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
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

  @PostMapping("/login")
  ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request);

  @PatchMapping("/user/{id}")
  ResponseEntity<UserResponse> updateUser(
      @PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest request);

  @PatchMapping("/user/{id}/change-password")
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

  @DeleteMapping("/user/{id}")
  ResponseEntity<UserResponse> deleteUser(@PathVariable("id") String id);
}
