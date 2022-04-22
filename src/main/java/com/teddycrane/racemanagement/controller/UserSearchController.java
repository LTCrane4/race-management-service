package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.services.UserSearchService;
import com.teddycrane.racemanagement.utils.mapper.UserMapper;
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
  public ResponseEntity<UserCollectionResponse> searchUsers(@NonNull SearchUserRequest request)
      throws BadRequestException {
    logger.info("Search users called");

    if (request.isValidRequest()) {
      return ResponseEntity.ok(
          UserMapper.convertEntityListToDTO(this.userSearchService.searchUsers(request)));
    } else {
      logger.error("Invalid search request provided");
      throw new BadRequestException("A valid request must be provided");
    }
  }
}
