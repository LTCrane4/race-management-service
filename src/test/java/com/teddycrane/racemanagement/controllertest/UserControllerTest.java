package com.teddycrane.racemanagement.controllertest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teddycrane.racemanagement.controller.UserController;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.request.UpdateUserRequest;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.model.user.response.GetAllUsersResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest {

  private UserController userController;

  private User expected;

  private UUID testId;

  private String testString;

  @Mock private UserService userService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    this.userController = new UserController(this.userService);
    this.expected = TestResourceGenerator.generateUser();
    testId = UUID.randomUUID();
    testString = testId.toString();
  }

  @Test
  public void userController_shouldConstruct() {
    assertNotNull(userController);
  }

  @Test
  public void getUser_shouldReturnUser() {
    when(this.userService.getUser(any(UUID.class)))
        .thenReturn(Optional.of(new User()));
    User result = this.userController.getUser(UUID.randomUUID().toString());
    assertNotNull(result);
  }

  @Test
  public void getUser_shouldThrowBadRequestErrorIfBadId() {
    assertThrows(BadRequestException.class,
                 () -> this.userController.getUser("test"));
  }

  @Test
  public void getUserShouldThrowNotFoundIfNoUserPresent() {
    when(this.userService.getUser(any(UUID.class)))
        .thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> this.userController.getUser(UUID.randomUUID().toString()));
  }

  @Test
  public void createUserShouldCreateAUser() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userService.createUser(anyString(), anyString(), anyString(),
                                     anyString(), anyString(),
                                     any(UserType.class)))
        .thenReturn(expected);

    User result = this.userController.createUser(
        new CreateUserRequest("", "", "", "", "", UserType.USER));
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void createUserShouldCreateUserWithouType() {
    when(this.userService.createUser(anyString(), anyString(), anyString(),
                                     anyString(), anyString(),
                                     eq(UserType.USER)))
        .thenReturn(expected);

    User actual = this.userController.createUser(
        new CreateUserRequest("", "", "", "", ""));

    assertNotNull(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void createUserShouldHandleServiceErrors() {
    when(this.userService.getUser(any(UUID.class)))
        .thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class,
        () -> this.userController.getUser(UUID.randomUUID().toString()));
  }

  @Test
  public void loginShouldAuthenticateUser() throws Exception {
    AuthenticationResponse expected = new AuthenticationResponse("valid token");
    when(this.userService.login(anyString(), anyString())).thenReturn(expected);

    AuthenticationResponse actual =
        this.userController.login(new AuthenticationRequest("test", "test"));
    assertEquals(actual, expected);
  }

  @Test
  public void loginShouldHandleExceptions() throws Exception {
    when(this.userService.login(anyString(), anyString()))
        .thenThrow(NotAuthorizedException.class);

    assertThrows(
        NotAuthorizedException.class,
        () -> this.userController.login(new AuthenticationRequest("", "")));
  }

  @Test
  public void getAllUsersShouldReturnCorrectResponse() {
    Collection<User> expectedList = TestResourceGenerator.generateUserList(5);
    when(this.userService.getAllUsers()).thenReturn(expectedList);

    GetAllUsersResponse actual = this.userController.getAllUsers();

    assertEquals(expectedList, actual.getUsers());
  }

  @Test
  public void updateUserShouldUpdateWithFullRequestBody() {
    when(this.userService.updateUser(any(UUID.class), anyString(), anyString(),
                                     anyString(), any(UserType.class)))
        .thenReturn(expected);

    User actual = this.userController.updateUser(
        UUID.randomUUID().toString(),
        new UpdateUserRequest("", "", "", UserType.ADMIN));

    assertEquals(expected, actual);
  }

  @Test
  public void updateUserWithType() {
    when(this.userService.updateUser(eq(testId), isNull(), isNull(), isNull(),
                                     any(UserType.class)))
        .thenReturn(expected);
    User actual = this.userController.updateUser(
        testString, new UpdateUserRequest(null, null, null, UserType.USER));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithEmail() {
    // email is null
    when(this.userService.updateUser(eq(testId), isNull(), isNull(),
                                     anyString(), isNull()))
        .thenReturn(expected);
    User actual = this.userController.updateUser(
        testString, new UpdateUserRequest(null, null, "", null));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithLastName() {
    when(this.userService.updateUser(eq(testId), isNull(), anyString(),
                                     isNull(), isNull()))
        .thenReturn(expected);
    User actual = this.userController.updateUser(
        testString, new UpdateUserRequest(null, "", null, null));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithFirstName() {
    when(this.userService.updateUser(eq(testId), anyString(), isNull(),
                                     isNull(), isNull()))
        .thenReturn(expected);
    User actual = this.userController.updateUser(
        testString, new UpdateUserRequest("", null, null, null));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithNoFirstOrLastName() {
    when(this.userService.updateUser(eq(testId), isNull(), isNull(),
                                     anyString(), any(UserType.class)))
        .thenReturn(expected);
    User actual = this.userController.updateUser(
        testString, new UpdateUserRequest(null, null, "", UserType.USER));
    assertEquals(expected, actual);
  }

  @Test
  void updateUserWithEmailOnly() {
    // userType is null and email is not null
    when(this.userService.updateUser(eq(testId), isNull(), isNull(),
                                     anyString(), isNull()))
        .thenReturn(expected);
    User actual = this.userController.updateUser(
        testString, new UpdateUserRequest(null, null, "", null));
    assertEquals(expected, actual);
  }

  @Test
  public void updateUserShouldHandleBadRequest() {
    assertThrows(
        BadRequestException.class,
        ()
            -> this.userController.updateUser(
                testString, new UpdateUserRequest(null, null, null, null)));

    assertThrows(BadRequestException.class,
                 ()
                     -> this.userController.updateUser(
                         "bad id", new UpdateUserRequest()));
  }
}
