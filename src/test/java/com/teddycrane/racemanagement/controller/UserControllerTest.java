package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teddycrane.racemanagement.enums.UserStatus;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.*;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.model.user.request.*;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.model.user.response.UserResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
  @DisplayName("Get user should throw an Illegal Argument Exception when the id is invalid")
  void getUserShouldThrowForInvalidId() {
    assertThrows(
        BadRequestException.class,
        () -> this.userController.getUser("asdf"),
        "The method should throw a BadRequestException");
  }

  @Test
  void createUserShouldCreateAUser() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userService.createUser(any(CreateUserRequest.class))).thenReturn(expected);

    var result =
        this.userController.createUser(
            new CreateUserRequest("", "", "", "", "", UserType.USER, UserStatus.ACTIVE));

    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.OK, result.getStatusCode(), "The response status should be 200"),
        () -> assertNotNull(body, "The response body should not be null"));
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
        () -> assertNotNull(body, "The response body should not be null"));
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
    assertThrows(
        BadRequestException.class,
        () -> this.userController.updateUser("bad id", new UpdateUserRequest()),
        "A BadRequestException should be thrown");
  }

  @Test
  void updateUserShouldHandleEmptyParamsUpdate() {
    assertThrows(
        BadRequestException.class,
        () ->
            this.userController.updateUser(
                testString,
                new UpdateUserRequest(null, null, null, null, Instant.now().toString())),
        "A BadRequestException should be thrown");
  }

  @Test
  void updateUserShouldNotUpdateUserIfTheUserTypeIsInsufficient() {
    this.expected = TestResourceGenerator.generateUser(UserType.USER);
    this.setUpSecurityContext(this.expected);

    assertThrows(
        ForbiddenException.class,
        () ->
            this.userController.updateUser(
                testString,
                new UpdateUserRequest(null, null, null, null, Instant.now().toString())),
        "A ForbiddenException should be thrown");
  }

  @Test
  @DisplayName("Update user should not update if timestamp cannot be converted")
  void updateUserShouldNotUpdateIfBadTimestamp() {
    assertThrows(
        BadRequestException.class,
        () ->
            this.userController.updateUser(
                testString, new UpdateUserRequest(null, null, null, null, null)),
        "A BadRequestException should be thrown");
  }

  @Test
  @DisplayName(
      "Update user should return a Conflict status code if the updated timestamp is out of date")
  void updateUserShouldThrowConflictException() {
    when(this.userService.updateUser(
            eq(testId),
            anyString(),
            anyString(),
            anyString(),
            any(UserType.class),
            any(Instant.class)))
        .thenThrow(ConflictException.class);

    assertThrows(
        ConflictException.class,
        () ->
            this.userController.updateUser(
                testString,
                new UpdateUserRequest("", "", "", UserType.USER, Instant.now().toString())),
        "A ConflictException should be thrown");
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

    assertThrows(
        NotFoundException.class,
        () ->
            this.userController.updateUser(
                testString,
                new UpdateUserRequest("", "", "", UserType.ADMIN, Instant.now().toString())),
        "A NotFoundException should be thrown");
  }

  @Test
  void oldUserSearchShouldReturnRedirect() {
    var response = this.userController.searchUsers(null);
    assertAll(
        () -> assertNotNull(response, "response should not be null"),
        () ->
            assertEquals(
                HttpStatus.MOVED_PERMANENTLY,
                response.getStatusCode(),
                "The status should be 301"));
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
    assertThrows(
        BadRequestException.class,
        () -> this.userController.deleteUser("not a valid id"),
        "A BadRequestException should be thrown");
  }

  @Test
  void deleteUserShouldThrowForInsufficientPermissions() {
    this.expected.setUserType(UserType.USER);
    this.setUpSecurityContext(this.expected);

    assertThrows(
        ForbiddenException.class,
        () -> this.userController.deleteUser(testString),
        "A ForbiddenException should be thrown");
  }

  @Test
  void deleteUserShouldNotAllowUsersToDeleteThemselves() {
    assertThrows(
        TransitionNotAllowedException.class,
        () -> this.userController.deleteUser(expected.getId().toString()),
        "A TransitionNotAllowedException should be thrown");
  }

  @Test
  void deleteUserShouldThrow404IfUserNotFound() {
    when(this.userService.deleteUser(any(UUID.class))).thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class,
        () -> this.userController.deleteUser(testString),
        "A NotFoundException should be thrown");
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
    assertThrows(
        ForbiddenException.class,
        () ->
            this.userController.changePassword(
                UUID.randomUUID().toString(),
                new ChangePasswordRequest("oldPassword", "newPassword")));
  }

  @Test
  void changePasswordShouldHandleServiceErrors() {
    when(this.userService.changePassword(eq(expected.getId()), anyString(), anyString()))
        .thenThrow(BadRequestException.class);

    ChangePasswordRequest request = new ChangePasswordRequest("old", "new");

    assertThrows(
        BadRequestException.class,
        () ->
            this.userController.changePassword(
                expected.getId().toString(), new ChangePasswordRequest("test", "Test")),
        "A BadRequestException should be thrown");

    assertThrows(
        BadRequestException.class,
        () -> this.userController.changePassword("not a uuid", request),
        "A BadRequestException should be thrown");
  }

  @Test
  @DisplayName("Change password should throw a NotFoundException if the user is not found")
  void changePasswordShouldReturn404IfUserNotFound() {
    when(this.userService.changePassword(eq(expected.getId()), anyString(), anyString()))
        .thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class,
        () ->
            this.userController.changePassword(
                expected.getId().toString(), new ChangePasswordRequest("old", "new")),
        "A NotFoundException should be thrown");
  }

  @Test
  @DisplayName("Change Status should return a 200")
  void changeStatusShouldReturn200() {
    when(this.userService.changeStatus(any(), any(), any())).thenReturn(expected);

    var result =
        this.userController.changeStatus(
            testString, new ChangeStatusRequest(UserStatus.ACTIVE, Instant.now().toString()));

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"));
  }

  @Test
  void changeStatusShouldReturn400() {
    assertThrows(
        BadRequestException.class,
        () ->
            this.userController.changeStatus(
                "bad", new ChangeStatusRequest(UserStatus.ACTIVE, Instant.now().toString())),
        "A BadRequestException should be thrown");
  }

  @Test
  void changeStatusShouldReturn404() {
    when(this.userService.changeStatus(eq(testId), any(), any()))
        .thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class,
        () ->
            this.userController.changeStatus(
                testString, new ChangeStatusRequest(UserStatus.ACTIVE, Instant.now().toString())),
        "A NotFoundException should be thrown");
  }

  @Test
  @Disabled
  void changeStatusShouldReturn405() {
    when(this.userService.changeStatus(eq(testId), any(), any()))
        .thenThrow(TransitionNotAllowedException.class);

    var result =
        this.userController.changeStatus(
            testString, new ChangeStatusRequest(UserStatus.ACTIVE, Instant.now().toString()));

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.METHOD_NOT_ALLOWED, result.getStatusCode(), "The status should be 405"));
  }
}
