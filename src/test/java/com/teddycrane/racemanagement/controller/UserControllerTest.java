package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.InternalServerError;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.user.request.ChangePasswordRequest;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.model.user.response.UserResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class UserControllerTest {

  private UserApi userController;

  private User expected;

  private UUID testId;

  private String testString;

  private UserPrincipal authPrincipal;

  @Mock private UserService userService;

  private void setUpSecurityContext(User principalUser) {
    this.authPrincipal = new UserPrincipal(principalUser);
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);

    SecurityContextHolder.setContext(securityContext);

    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
        .thenReturn(authPrincipal);
  }

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    this.userController = new UserController(this.userService);
    this.expected = TestResourceGenerator.generateUser(UserType.ADMIN);
    this.testId = UUID.randomUUID();
    this.testString = testId.toString();
    this.setUpSecurityContext(this.expected);
  }

  @Test
  void userController_shouldConstruct() {
    assertNotNull(userController);
  }

  @Test
  void getUserShouldReturnUser() {
    when(this.userService.getUser(any(UUID.class))).thenReturn(expected);
    var result = this.userController.getUser(UUID.randomUUID().toString());
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.OK, result.getStatusCode(), "The response status should be 200"),
        () -> assertNotNull(body, "The response body should not be null"),
        () ->
            assertFalse(
                result.toString().contains("password"),
                "The response should not include a password"));
  }

  @Test
  void getUserShouldReturn400IfIdIsNotValid() {
    var result = this.userController.getUser("not a uuid");

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status should be 400"));
  }

  @Test
  void getUserShouldReturn404IfUserNotFound() {
    when(this.userService.getUser(any(UUID.class))).thenThrow(NotFoundException.class);
    var result = this.userController.getUser(testString);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.NOT_FOUND, result.getStatusCode(), "The response status should be 404"));
  }

  @Test
  void createUserShouldCreateAUser() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userService.createUser(any(CreateUserRequest.class))).thenReturn(expected);

    var result =
        this.userController.createUser(new CreateUserRequest("", "", "", "", "", UserType.USER));

    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.OK, result.getStatusCode(), "The response status should be 200"),
        () -> assertNotNull(body, "The response body should not be null"),
        () ->
            assertEquals(
                expected.getUsername(), body.getUsername(), "The usernames should be equal"),
        () ->
            assertEquals(
                expected.getUserType(), body.getUserType(), "The user types should be equal"));
  }

  @Test
  void createUserShouldCreateUserWithoutType() {
    when(this.userService.createUser(any(CreateUserRequest.class))).thenReturn(expected);

    var actual = this.userController.createUser(new CreateUserRequest("", "", "", "", ""));
    var body = actual.getBody();

    assertAll(
        () -> assertNotNull(actual),
        () ->
            assertEquals(
                HttpStatus.OK, actual.getStatusCode(), "The response status should be 200"),
        () -> assertNotNull(body, "The response body should not be null"),
        () ->
            assertEquals(
                expected.getUsername(), body.getUsername(), "The usernames should be equal"));
  }

  @Test
  @DisplayName("Create user should return a 409 when another user exists")
  void createUserShouldReturnConflict() {
    when(this.userService.createUser(any(CreateUserRequest.class)))
        .thenThrow(DuplicateItemException.class);

    var result = this.userController.createUser(new CreateUserRequest());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.CONFLICT, result.getStatusCode(), "The status code should be 409"));
  }

  @Test
  void loginShouldAuthenticateUser() throws Exception {
    AuthenticationResponse expected = new AuthenticationResponse("valid token");
    when(this.userService.login(anyString(), anyString())).thenReturn(expected);

    var actual = this.userController.login(new AuthenticationRequest("test", "test"));
    assertAll(
        () -> assertNotNull(actual, "The response should not be null"),
        () ->
            assertEquals(
                HttpStatus.OK, actual.getStatusCode(), "The response status should be 200"));
  }

  @Test
  void loginShouldHandleExceptions() {
    when(this.userService.login(anyString(), anyString())).thenThrow(NotAuthorizedException.class);

    var result = this.userController.login(new AuthenticationRequest("", ""));

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.UNAUTHORIZED, result.getStatusCode(), "The status should be 401"));
  }

  @Test
  void getAllUsersShouldReturnCorrectResponse() {
    Collection<User> expectedList = TestResourceGenerator.generateUserList(5);
    when(this.userService.getAllUsers()).thenReturn(expectedList);

    UserCollectionResponse actual = this.userController.getAllUsers().getBody();

    assertAll(
        () -> assertNotNull(actual, "The response entity body should not be null"),
        () -> assertEquals(new UserCollectionResponse(expectedList).getUsers(), actual.getUsers()));
  }

  @Test
  void updateUserShouldUpdateWithFullRequestBody() {
    when(this.userService.updateUser(
            any(UUID.class),
            anyString(),
            anyString(),
            anyString(),
            any(UserType.class),
            any(Instant.class)))
        .thenReturn(expected);

    var result =
        this.userController.updateUser(
            UUID.randomUUID().toString(),
            new UpdateUserRequest(
                "", "", "", UserType.ADMIN, expected.getUpdatedTimestamp().toString()));
    var actual = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(actual, "The body should not be null"),
        () ->
            assertEquals(
                expected.getUsername(),
                actual.getUsername(),
                "The username should match the expected one"),
        () ->
            assertEquals(
                expected.getFirstName(), actual.getFirstName(), "The first names should be equal"),
        () -> assertEquals(expected.getLastName(), actual.getLastName()),
        () -> assertEquals(expected.getUserType(), actual.getUserType()));
  }

  @Test
  void updateUserWithType() {
    when(this.userService.updateUser(
            any(UUID.class), isNull(), isNull(), isNull(), any(UserType.class), any(Instant.class)))
        .thenReturn(expected);

    var response =
        this.userController.updateUser(
            testString,
            new UpdateUserRequest(
                null, null, null, UserType.USER, expected.getUpdatedTimestamp().toString()));
    var body = response.getBody();
    assertAll(
        () -> assertNotNull(response, "The response should not be null"),
        () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "The status should be 200"),
        () -> assertNotNull(body, "The response body should not be null"),
        () ->
            assertEquals(
                expected.getUsername(),
                body.getUsername(),
                "The username should match the expected one"),
        () ->
            assertEquals(
                expected.getFirstName(), body.getFirstName(), "The first names should be equal"),
        () -> assertEquals(expected.getLastName(), body.getLastName()),
        () -> assertEquals(expected.getUserType(), body.getUserType()));
  }

  @Test
  void updateUserWithEmail() {
    when(this.userService.updateUser(
            any(UUID.class), isNull(), isNull(), anyString(), isNull(), any(Instant.class)))
        .thenReturn(expected);

    var response =
        this.userController.updateUser(
            testString,
            new UpdateUserRequest(null, null, "", null, expected.getUpdatedTimestamp().toString()));
    var body = response.getBody();

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "The status should be 200"),
        () ->
            assertEquals(
                expected.getUsername(),
                body.getUsername(),
                "The username should match the expected one"),
        () ->
            assertEquals(
                expected.getFirstName(), body.getFirstName(), "The first names should be equal"),
        () -> assertEquals(expected.getLastName(), body.getLastName()),
        () -> assertEquals(expected.getUserType(), body.getUserType()));
  }

  @Test
  void updateUserWithLastName() {
    when(this.userService.updateUser(
            any(UUID.class), isNull(), anyString(), isNull(), isNull(), any(Instant.class)))
        .thenReturn(expected);

    var response =
        this.userController.updateUser(
            testString,
            new UpdateUserRequest(null, "", null, null, expected.getUpdatedTimestamp().toString()));
    var body = response.getBody();

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
        () ->
            assertEquals(
                expected.getUsername(),
                body.getUsername(),
                "The username should match the expected one"),
        () ->
            assertEquals(
                expected.getFirstName(), body.getFirstName(), "The first names should be equal"),
        () -> assertEquals(expected.getLastName(), body.getLastName()),
        () -> assertEquals(expected.getUserType(), body.getUserType()));
  }

  @Test
  void updateUserWithFirstName() {
    when(this.userService.updateUser(
            any(UUID.class), anyString(), isNull(), isNull(), isNull(), any(Instant.class)))
        .thenReturn(expected);

    var response =
        this.userController.updateUser(
            testString,
            new UpdateUserRequest("", null, null, null, expected.getUpdatedTimestamp().toString()));
    var body = response.getBody();

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
        () ->
            assertEquals(
                expected.getUsername(),
                body.getUsername(),
                "The username should match the expected one"),
        () ->
            assertEquals(
                expected.getFirstName(), body.getFirstName(), "The first names should be equal"),
        () -> assertEquals(expected.getLastName(), body.getLastName()),
        () -> assertEquals(expected.getUserType(), body.getUserType()));
  }

  @Test
  void updateUserWithNoFirstOrLastName() {
    when(this.userService.updateUser(
            any(UUID.class),
            isNull(),
            isNull(),
            anyString(),
            any(UserType.class),
            any(Instant.class)))
        .thenReturn(expected);

    var response =
        this.userController.updateUser(
            testString,
            new UpdateUserRequest(
                null, null, "", UserType.USER, expected.getUpdatedTimestamp().toString()));
    var body = response.getBody();

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
        () ->
            assertEquals(
                expected.getUsername(),
                body.getUsername(),
                "The username should match the expected one"),
        () ->
            assertEquals(
                expected.getFirstName(), body.getFirstName(), "The first names should be equal"),
        () -> assertEquals(expected.getLastName(), body.getLastName()),
        () -> assertEquals(expected.getUserType(), body.getUserType()));
  }

  @Test
  void updateUserWithEmailOnly() {
    // userType is null and email is not null
    when(this.userService.updateUser(
            any(UUID.class), isNull(), isNull(), anyString(), isNull(), any(Instant.class)))
        .thenReturn(expected);

    var response =
        this.userController.updateUser(
            testString,
            new UpdateUserRequest(null, null, "", null, expected.getUpdatedTimestamp().toString()));
    var body = response.getBody();

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
        () ->
            assertEquals(
                expected.getUsername(),
                body.getUsername(),
                "The username should match the expected one"),
        () ->
            assertEquals(
                expected.getFirstName(), body.getFirstName(), "The first names should be equal"),
        () -> assertEquals(expected.getLastName(), body.getLastName()),
        () -> assertEquals(expected.getUserType(), body.getUserType()));
  }

  @Test
  void updateUserShouldHandleBadUserId() {
    var response = this.userController.updateUser("bad id", new UpdateUserRequest());

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()));
  }

  @Test
  void updateUserShouldHandleEmptyParamsUpdate() {
    var response =
        this.userController.updateUser(
            testString, new UpdateUserRequest(null, null, null, null, Instant.now().toString()));

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()));
  }

  @Test
  void updateUserShouldNotUpdateUserIfTheUserTypeIsInsufficient() {
    this.expected = TestResourceGenerator.generateUser(UserType.USER);
    this.setUpSecurityContext(this.expected);

    var response =
        this.userController.updateUser(
            testString, new UpdateUserRequest(null, null, null, null, Instant.now().toString()));

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode()));
  }

  @Test
  @DisplayName("Update user should not update if timestamp cannot be converted")
  void updateUserShouldNotUpdateIfBadTimestamp() {
    var response =
        this.userController.updateUser(
            testString, new UpdateUserRequest(null, null, null, null, null));

    assertAll(
        () -> assertNotNull(response, "The response should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, response.getStatusCode(), "The status should be 400"));
  }

  @Test
  @DisplayName(
      "Update user should return a Conflict status code if the updated timestamp is out of date")
  void updateUserShouldReturn409IfTheTimeIsOutOfDate() {
    when(this.userService.updateUser(
            eq(testId),
            anyString(),
            anyString(),
            anyString(),
            any(UserType.class),
            any(Instant.class)))
        .thenThrow(ConflictException.class);

    var result =
        this.userController.updateUser(
            testString, new UpdateUserRequest("", "", "", UserType.USER, Instant.now().toString()));

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.CONFLICT, result.getStatusCode(), "The status should be 409"));
  }

  @Test
  @DisplayName("Update user should handle Not Found exceptions")
  void updateUserShouldHandleNotFound() {
    when(this.userService.updateUser(
            eq(testId),
            anyString(),
            anyString(),
            anyString(),
            any(UserType.class),
            any(Instant.class)))
        .thenThrow(NotFoundException.class);

    var result =
        this.userController.updateUser(
            testString,
            new UpdateUserRequest("", "", "", UserType.ADMIN, Instant.now().toString()));

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode(), "The status should be 404"));
  }

  @Test
  @DisplayName("Update user should handle internal server errors")
  void updateUserShouldHandleInternalServerError() {
    when(this.userService.updateUser(
            eq(testId),
            anyString(),
            anyString(),
            anyString(),
            any(UserType.class),
            any(Instant.class)))
        .thenThrow(InternalServerError.class);

    var result =
        this.userController.updateUser(
            testString,
            new UpdateUserRequest("", "", "", UserType.ADMIN, Instant.now().toString()));

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                result.getStatusCode(),
                "The status should be 500"));
  }

  @Test
  void shouldSearchUsers() {
    when(this.userService.searchUsers(any(SearchType.class), anyString()))
        .thenReturn(TestResourceGenerator.generateUserList(5));

    var response = this.userController.searchUsers(SearchType.TYPE, "test");
    assertNotNull(response, "response should not be null");
  }

  @Test
  void shouldReturn400IfSearchTypeAndValueMismatched() {
    when(this.userService.searchUsers(SearchType.TYPE, "not a type"))
        .thenThrow(IllegalArgumentException.class);

    var result = this.userController.searchUsers(SearchType.TYPE, "not a type");

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status code should be 400"));
  }

  @Test
  void deleteUserShouldDelete() {
    when(this.userService.deleteUser(testId)).thenReturn(expected);

    var actual = this.userController.deleteUser(testString);
    var body = actual.getBody();

    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, actual.getStatusCode(), "The status code should be 200"),
        () ->
            assertEquals(
                new UserResponse(expected), body, "The result should match the expected value"));
  }

  @Test
  void deleteUserShouldHandleBadId() {
    var result = this.userController.deleteUser("not a valid id");
    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status should be 400"));
  }

  @Test
  void deleteUserShouldThrowForInsufficientPermissions() {
    this.expected.setUserType(UserType.USER);
    this.setUpSecurityContext(this.expected);

    var result = this.userController.deleteUser(testString);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode(), "The status should be 403"));
  }

  @Test
  void deleteUserShouldNotAllowUsersToDeleteThemselves() {
    var result = this.userController.deleteUser(expected.getId().toString());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status code should be 400"));
  }

  @Test
  void deleteUserShouldThrow404IfUserNotFound() {
    when(this.userService.deleteUser(any(UUID.class))).thenThrow(NotFoundException.class);

    var result = this.userController.deleteUser(testString);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode(), "The status should be 404"));
  }

  @Test
  void changePasswordShouldChangePassword() {
    UUID id = expected.getId();
    when(this.userService.changePassword(id, expected.getPassword(), "new password"))
        .thenReturn(true);
    String encodedOld = Base64.getEncoder().encodeToString(expected.getPassword().getBytes());
    String encodedNew = Base64.getEncoder().encodeToString("new password".getBytes());

    var result =
        this.userController.changePassword(
            id.toString(), new ChangePasswordRequest(encodedOld, encodedNew));

    assertAll(() -> assertNotNull(result, "The result should not be null"));
  }

  @Test
  void changePasswordShouldReturn403IfUserChangesOtherUsersPassword() {
    var result =
        this.userController.changePassword(
            UUID.randomUUID().toString(), new ChangePasswordRequest("oldPassword", "newPassword"));

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.FORBIDDEN, result.getStatusCode(), "The status code should be 403"));
  }

  @Test
  void changePasswordShouldHandleServiceErrors() {
    when(this.userService.changePassword(eq(expected.getId()), anyString(), anyString()))
        .thenThrow(BadRequestException.class);

    ChangePasswordRequest request = new ChangePasswordRequest("old", "new");

    var result1 =
        this.userController.changePassword(
            expected.getId().toString(), new ChangePasswordRequest("test", "Test"));

    assertAll(
        () -> assertNotNull(result1, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result1.getStatusCode(), "The status code should be 400"));

    var result2 = this.userController.changePassword("not a uuid", request);

    assertAll(
        () -> assertNotNull(result2, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result2.getStatusCode(), "The status should be 400"));
  }

  @Test
  void changePasswordShouldReturn404IfUserNotFound() {
    when(this.userService.changePassword(eq(expected.getId()), anyString(), anyString()))
        .thenThrow(NotFoundException.class);

    var result =
        this.userController.changePassword(
            expected.getId().toString(), new ChangePasswordRequest("old", "new"));

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.NOT_FOUND, result.getStatusCode(), "The status code should be 404"));
  }
}
