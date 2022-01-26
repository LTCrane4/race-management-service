package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.InsufficientPermissionsException;
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
import java.util.Collection;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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
  void getUser_shouldReturnUser() {
    when(this.userService.getUser(any(UUID.class))).thenReturn(expected);
    var result = this.userController.getUser(UUID.randomUUID().toString());
    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.OK, result.getStatusCode(), "The response status should be 200"),
        () -> assertNotNull(result.getBody(), "The response body should not be null"),
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
  void createUserShouldCreateUserWithouType() {
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
    when(this.userService.updateUser(any(UUID.class), any(UpdateUserRequest.class)))
        .thenReturn(expected);

    var actual =
        this.userController.updateUser(
            UUID.randomUUID().toString(), new UpdateUserRequest("", "", "", UserType.ADMIN));

    assertAll(
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
    when(this.userService.updateUser(eq(testId), any(UpdateUserRequest.class)))
        .thenReturn(expected);
    var actual =
        this.userController.updateUser(
            testString, new UpdateUserRequest(null, null, null, UserType.USER));
    assertAll(
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
  void updateUserWithEmail() {
    // email is null
    when(this.userService.updateUser(eq(testId), any(UpdateUserRequest.class)))
        .thenReturn(expected);
    var actual =
        this.userController.updateUser(testString, new UpdateUserRequest(null, null, "", null));
    assertAll(
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
  void updateUserWithLastName() {
    when(this.userService.updateUser(eq(testId), any(UpdateUserRequest.class)))
        .thenReturn(expected);
    var actual =
        this.userController.updateUser(testString, new UpdateUserRequest(null, "", null, null));
    assertAll(
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
  void updateUserWithFirstName() {
    when(this.userService.updateUser(eq(testId), any(UpdateUserRequest.class)))
        .thenReturn(expected);
    var actual =
        this.userController.updateUser(testString, new UpdateUserRequest("", null, null, null));
    assertAll(
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
  void updateUserWithNoFirstOrLastName() {
    when(this.userService.updateUser(eq(testId), any(UpdateUserRequest.class)))
        .thenReturn(expected);
    var actual =
        this.userController.updateUser(
            testString, new UpdateUserRequest(null, null, "", UserType.USER));
    assertAll(
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
  void updateUserWithEmailOnly() {
    // userType is null and email is not null
    when(this.userService.updateUser(eq(testId), any(UpdateUserRequest.class)))
        .thenReturn(expected);
    var actual =
        this.userController.updateUser(testString, new UpdateUserRequest(null, null, "", null));
    assertAll(
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
  void updateUserShouldHandleBadRequest() {
    assertThrows(
        BadRequestException.class,
        () ->
            this.userController.updateUser(
                testString, new UpdateUserRequest(null, null, null, null)));

    assertThrows(
        BadRequestException.class,
        () -> this.userController.updateUser("bad id", new UpdateUserRequest()));
  }

  @Test
  void updateUserShouldNotUpdateUserIfTheUserTypeIsInsufficient() {
    this.expected = TestResourceGenerator.generateUser(UserType.USER);
    this.setUpSecurityContext(this.expected);

    assertThrows(
        InsufficientPermissionsException.class,
        () ->
            this.userController.updateUser(
                testString, new UpdateUserRequest(null, null, null, null)),
        "Users with the user type of USER should not be able to update other users");
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

    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () ->
            assertEquals(
                new UserResponse(expected), actual, "The result should match the expected value"));
  }

  @Test
  void deleteUserShouldHandleBadId() {
    assertThrows(
        BadRequestException.class,
        () -> this.userController.deleteUser("not uuid"),
        "Invalid UUID values should throw an exception");
  }

  @Test
  void deleteUserShouldThrowForInsufficientPermissions() {
    this.expected.setUserType(UserType.USER);
    this.setUpSecurityContext(this.expected);

    assertThrows(
        InsufficientPermissionsException.class,
        () -> this.userController.deleteUser(testString),
        "A user with the type of USER should not be able to delete users");
  }

  @Test
  void deleteUserShouldNotAllowUsersToDeleteThemselves() {
    assertThrows(
        BadRequestException.class,
        () -> this.userController.deleteUser(expected.getId().toString()),
        "Users should not be able to delete themselves");
  }

  @Test
  void changePasswordShouldChangePassword() {
    UUID id = expected.getId();
    String idString = expected.getId().toString();

    when(this.userService.changePassword(id, expected.getPassword(), "new password"))
        .thenReturn(true);

    var result =
        this.userController.changePassword(
            id.toString(), new ChangePasswordRequest(expected.getPassword(), "new password"));

    assertAll(() -> assertNotNull(result, "The result should not be null"));
  }
}
