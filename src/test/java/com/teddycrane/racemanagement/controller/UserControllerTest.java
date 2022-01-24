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
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.util.Collection;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class UserControllerTest {

  private UserController userController;

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
    when(this.userService.getUser(any(UUID.class))).thenReturn(new User());
    var result = this.userController.getUser(UUID.randomUUID().toString());
    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertFalse(
                result.toString().contains("password"),
                "The response should not include a password"));
  }

  @Test
  void getUser_shouldThrowBadRequestErrorIfBadId() {
    assertThrows(BadRequestException.class, () -> this.userController.getUser("test"));
  }

  @Test
  void getUserShouldThrowNotFoundIfNoUserPresent() {
    when(this.userService.getUser(any(UUID.class))).thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class, () -> this.userController.getUser(UUID.randomUUID().toString()));
  }

  @Test
  void createUserShouldCreateAUser() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userService.createUser(
            anyString(), anyString(), anyString(), anyString(), anyString(), any(UserType.class)))
        .thenReturn(expected);

    User result =
        this.userController.createUser(new CreateUserRequest("", "", "", "", "", UserType.USER));
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  void createUserShouldCreateUserWithouType() {
    when(this.userService.createUser(
            anyString(), anyString(), anyString(), anyString(), anyString(), eq(UserType.USER)))
        .thenReturn(expected);

    User actual = this.userController.createUser(new CreateUserRequest("", "", "", "", ""));

    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  void createUserShouldHandleServiceErrors() {
    when(this.userService.getUser(any(UUID.class))).thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class, () -> this.userController.getUser(UUID.randomUUID().toString()));
  }

  @Test
  void loginShouldAuthenticateUser() throws Exception {
    AuthenticationResponse expected = new AuthenticationResponse("valid token");
    when(this.userService.login(anyString(), anyString())).thenReturn(expected);

    AuthenticationResponse actual =
        this.userController.login(new AuthenticationRequest("test", "test"));
    assertEquals(actual, expected);
  }

  @Test
  void loginShouldHandleExceptions() throws Exception {
    when(this.userService.login(anyString(), anyString())).thenThrow(NotAuthorizedException.class);

    assertThrows(
        NotAuthorizedException.class,
        () -> this.userController.login(new AuthenticationRequest("", "")));
  }

  @Test
  void getAllUsersShouldReturnCorrectResponse() {
    Collection<User> expectedList = TestResourceGenerator.generateUserList(5);
    when(this.userService.getAllUsers()).thenReturn(expectedList);

    UserCollectionResponse actual = this.userController.getAllUsers();

    assertEquals(new UserCollectionResponse(expectedList).getUsers(), actual.getUsers());
  }

  @Test
  void updateUserShouldUpdateWithFullRequestBody() {
    when(this.userService.updateUser(
            any(UUID.class), anyString(), anyString(), anyString(), any(UserType.class)))
        .thenReturn(expected);

    User actual =
        this.userController.updateUser(
            UUID.randomUUID().toString(), new UpdateUserRequest("", "", "", UserType.ADMIN));

    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithType() {
    when(this.userService.updateUser(eq(testId), isNull(), isNull(), isNull(), any(UserType.class)))
        .thenReturn(expected);
    User actual =
        this.userController.updateUser(
            testString, new UpdateUserRequest(null, null, null, UserType.USER));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithEmail() {
    // email is null
    when(this.userService.updateUser(eq(testId), isNull(), isNull(), anyString(), isNull()))
        .thenReturn(expected);
    User actual =
        this.userController.updateUser(testString, new UpdateUserRequest(null, null, "", null));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithLastName() {
    when(this.userService.updateUser(eq(testId), isNull(), anyString(), isNull(), isNull()))
        .thenReturn(expected);
    User actual =
        this.userController.updateUser(testString, new UpdateUserRequest(null, "", null, null));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithFirstName() {
    when(this.userService.updateUser(eq(testId), anyString(), isNull(), isNull(), isNull()))
        .thenReturn(expected);
    User actual =
        this.userController.updateUser(testString, new UpdateUserRequest("", null, null, null));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithNoFirstOrLastName() {
    when(this.userService.updateUser(
            eq(testId), isNull(), isNull(), anyString(), any(UserType.class)))
        .thenReturn(expected);
    User actual =
        this.userController.updateUser(
            testString, new UpdateUserRequest(null, null, "", UserType.USER));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithEmailOnly() {
    // userType is null and email is not null
    when(this.userService.updateUser(eq(testId), isNull(), isNull(), anyString(), isNull()))
        .thenReturn(expected);
    User actual =
        this.userController.updateUser(testString, new UpdateUserRequest(null, null, "", null));
    assertEquals(expected, actual);
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
  void shouldThrowErrorIfSearchTypeAndValueMismatched() {
    when(this.userService.searchUsers(SearchType.TYPE, "not a type"))
        .thenThrow(IllegalArgumentException.class);
    assertThrows(
        BadRequestException.class,
        () -> this.userController.searchUsers(SearchType.TYPE, "not a type"));
  }

  @Test
  void deleteUserShouldDelete() {
    when(this.userService.deleteUser(testId)).thenReturn(expected);

    var actual = this.userController.deleteUser(testString);

    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () -> assertEquals(expected, actual, "The result should match the expected value"));
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
}