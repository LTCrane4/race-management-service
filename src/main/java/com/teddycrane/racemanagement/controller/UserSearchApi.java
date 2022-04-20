package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface UserSearchApi {
  @PostMapping(
      value = "/users/search",
      consumes = "application/json",
      produces = "application/json")
  @Operation(summary = "Search Users", description = "New user search endpoint")
  ResponseEntity<? extends Response> searchUsers(@Valid @RequestBody SearchUserRequest request)
      throws BadRequestException;
}
