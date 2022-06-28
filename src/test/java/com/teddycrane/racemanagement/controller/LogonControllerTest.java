package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class LogonControllerTest {
  private LogonApi loginController;

  private User expected;
  private UserPrincipal authUser;

  @Mock private UserService userService;

  private void setupSecurityContext(User principal) {
    this.authUser = new UserPrincipal(principal);
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);

    SecurityContextHolder.setContext(securityContext);

    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
        .thenReturn(authUser);
  }

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    this.loginController = new LogonController(this.userService);
    this.expected = TestResourceGenerator.generateUser(UserType.ADMIN);
    this.setupSecurityContext(this.expected);
  }

  @Test
  void loginShouldAuthenticateUser() {
    AuthenticationResponse expected = new AuthenticationResponse("valid token");
    when(this.userService.login(anyString(), anyString())).thenReturn(expected);

    var request = Map.of("username", "test", "password", "test");

    var actual = this.loginController.login(request);
    assertAll(
        () -> assertNotNull(actual, "The response should not be null"),
        () ->
            assertEquals(
                HttpStatus.OK, actual.getStatusCode(), "The response status should be 200"));
  }

  @Test
  void loginShouldHandleExceptions() {
    when(this.userService.login(anyString(), anyString())).thenThrow(NotAuthorizedException.class);
    var request = Map.of("username", "", "password", "");

    assertThrows(NotAuthorizedException.class, () -> this.loginController.login(request));
  }
}
