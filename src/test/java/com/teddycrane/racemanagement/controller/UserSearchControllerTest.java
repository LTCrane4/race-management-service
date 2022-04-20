package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import com.teddycrane.racemanagement.services.UserSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class UserSearchControllerTest {
  private UserSearchApi userSearchController;

  @Mock private UserSearchService userSearchService;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    this.userSearchController = new UserSearchController(this.userSearchService);
  }

  @Test
  @DisplayName("Should search users and return a valid response when given a valid request")
  void shouldSearchUsers() {
    when(this.userSearchService.searchUsers(any()))
        .thenReturn(TestResourceGenerator.generateUserList(5));

    var response =
        this.userSearchController.searchUsers(
            SearchUserRequest.builder().userType(UserType.ADMIN).build());

    assertAll(
        () -> assertNotNull(response, "The response should not be null"),
        () ->
            assertEquals(HttpStatus.OK, response.getStatusCode(), "The status code should be 200"));
  }

  @Test
  @DisplayName("Should return a 400 when an invalid request is given")
  void shouldReturn400WhenInvalidRequest() {
    assertThrows(
        BadRequestException.class,
        () -> this.userSearchController.searchUsers(new SearchUserRequest()),
        "A BadRequestException should be thrown");
  }
}
