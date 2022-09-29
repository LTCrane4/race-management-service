package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully found users",
        content = @Content(schema = @Schema(implementation = UserCollectionResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<UserCollectionResponse> searchUsers(@Valid @RequestBody SearchUserRequest request)
      throws BadRequestException;
}
