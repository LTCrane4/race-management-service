package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.user.request.ChangePasswordRequest;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.model.user.response.ChangePasswordResponse;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.model.user.response.UserResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface UserApi {

  @GetMapping("/user")
  ResponseEntity<UserCollectionResponse> getAllUsers();

  @GetMapping("/user/{id}")
  ResponseEntity<UserResponse> getUser(@RequestParam String id);

  @GetMapping("/user/search")
  ResponseEntity<UserCollectionResponse> searchUsers(
      @RequestParam("type") SearchType searchType, @RequestParam("value") String searchValue);

  @PostMapping("/user/new")
  ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request);

  @PostMapping("/login")
  ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request);

  @PatchMapping("/user/{id}")
  UserResponse updateUser(
      @RequestParam("id") String id, @Valid @RequestBody UpdateUserRequest request);

  @PatchMapping("/user/{id}/change-password")
  ChangePasswordResponse changePassword(
      @RequestParam("id") String id, @Valid @RequestBody ChangePasswordRequest request);

  @DeleteMapping("/user/{id}")
  UserResponse deleteUser(@RequestParam("id") String id);
}
