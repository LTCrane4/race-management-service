package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.services.UserSearchService;
import com.teddycrane.racemanagement.utils.ResponseStatusGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSearchController extends BaseController implements UserSearchApi {

  private final UserSearchService userSearchService;

  public UserSearchController(UserSearchService userSearchService) {
    super();
    this.userSearchService = userSearchService;
  }

  @Override
  public ResponseEntity<? extends Response> searchUsers(@NonNull SearchUserRequest request) {
    logger.info("Search users called");

    if (request.isValidRequest()) {
      try {
        return ResponseEntity.ok(
            new UserCollectionResponse(this.userSearchService.searchUsers(request)));
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder().message("An unexpected error occurred").build());
      }
    } else {
      return ResponseStatusGenerator.generateBadRequestResponse("A valid request must be provided");
    }
  }
}
