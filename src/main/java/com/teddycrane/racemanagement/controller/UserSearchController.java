package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.services.UserSearchService;
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
  public ResponseEntity<? extends Response> searchUsers(@NonNull SearchUserRequest request)
      throws BadRequestException {
    logger.info("Search users called");

    if (request.isValidRequest()) {
      return ResponseEntity.ok(
          new UserCollectionResponse(this.userSearchService.searchUsers(request)));
    } else {
      throw new BadRequestException("A valid request must be provided");
    }
  }
}
