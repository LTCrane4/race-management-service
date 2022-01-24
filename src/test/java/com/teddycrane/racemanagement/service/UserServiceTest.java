package com.teddycrane.racemanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.security.util.TokenManager;
import com.teddycrane.racemanagement.services.UserService;
import com.teddycrane.racemanagement.services.UserServiceImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private TokenManager tokenManager;
  @Mock private AuthenticationManager authenticationManager;

  // Mock handlers
  @Mock private Handler<UUID, User> getUserHandler;
  @Mock private Handler<String, Collection<User>> getUsersHandler;
  @Mock private Handler<CreateUserRequest, User> createUserHandler;

  private UUID testId;

  private UserService userService;

  private User existing;

  @Captor private ArgumentCaptor<User> user;

  @BeforeEach
  void setUp() {
    this.userService =
        new UserServiceImpl(
            this.userRepository,
            this.tokenManager,
            this.authenticationManager,
            this.getUserHandler,
            this.getUsersHandler,
            this.createUserHandler);
    this.existing = TestResourceGenerator.generateUser();
    this.testId = UUID.randomUUID();
  }

  @Test
  void getUserShouldReturnUser() {
    when(this.getUserHandler.resolve(testId)).thenReturn(existing);

    var result = this.userService.getUser(testId);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(existing, result, "The actual value should equal the expected one"));
  }

  @Test
  void getUserShouldThrowErrorIfNotFound() {
    assertThrows(
        NotFoundException.class,
        () -> this.userService.getUser(testId),
        "It should throw an exception if the user is not found");
  }

  @Test
  void createUserShouldCreate() {
    User expected = TestResourceGenerator.generateUser();
    when(this.createUserHandler.resolve(any(CreateUserRequest.class))).thenReturn(expected);

    User actual =
        this.userService.createUser(new CreateUserRequest("", "", "", "", "", UserType.USER));
    assertEquals(expected, actual);
  }

  @Test
  public void createUserShouldCreateWithNoType() {
    User expected = TestResourceGenerator.generateUser();
    when(this.createUserHandler.resolve(any(CreateUserRequest.class))).thenReturn(expected);

    User actual = this.userService.createUser(new CreateUserRequest("", "", "", "", "", null));
    assertEquals(expected, actual);
    assertEquals("user", actual.getUserType().toString());
  }

  @Test
  void loginShouldReturnToken() throws Exception {
    when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.of(this.existing));

    AuthenticationResponse response =
        this.userService.login(existing.getUsername(), existing.getPassword());
    assertNotNull(response);
  }

  @Test
  void loginShouldHandleUserNotFound() {
    when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(NotAuthorizedException.class, () -> this.userService.login("test", "test"));
  }

  @Test
  public void getAllUsersShouldReturnListOfUsers() {
    Collection<User> expectedList = TestResourceGenerator.generateUserList(5);
    when(this.getUsersHandler.resolve(anyString())).thenReturn(expectedList);

    Collection<User> result = this.userService.getAllUsers();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                expectedList.size(), result.size(), "The result should have the expected length"));
  }

  @Test
  void updateUser() {
    when(this.userRepository.findById(testId)).thenReturn(Optional.of(existing));
    when(this.userRepository.save(any(User.class))).thenAnswer((input) -> input.getArguments()[0]);

    var actual =
        this.userService.updateUser(testId, "firstName", "lastName", "email", UserType.USER);
    verify(this.userRepository).save(user.capture());

    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () ->
            assertEquals(actual, user.getValue(), "The saved user should match the user returned"));
  }

  @Test
  void updateUserWithNoParams() {
    when(this.userRepository.findById(testId)).thenReturn(Optional.of(existing));
    when(this.userRepository.save(any(User.class))).thenAnswer((input) -> input.getArguments()[0]);

    var actual = this.userService.updateUser(testId, null, null, null, null);
    verify(this.userRepository).save(user.capture());

    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () -> assertEquals(existing, user.getValue(), "The user should not be updated"),
        () -> assertEquals(existing, actual, "The result should not have been updated"));
  }

  @Test
  void updateUserWithoutExistingUser() {
    when(this.userRepository.findById(testId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> this.userService.updateUser(testId, null, null, null, null));
  }

  @Test
  void shouldReturnUsersFromTypeSearch() {
    List<User> expectedList = new ArrayList<>(TestResourceGenerator.generateUserList(3));
    when(this.userRepository.findAllByUserType(UserType.USER)).thenReturn(expectedList);

    List<User> actual = new ArrayList<>(this.userService.searchUsers(SearchType.TYPE, "USER"));

    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () -> assertEquals(expectedList, actual));
  }

  @Test
  void shouldReturnUsersFromNameSearch() {
    List<User> expectedList = new ArrayList<>(TestResourceGenerator.generateUserList(3));
    when(this.userRepository.findAllByLastName(anyString())).thenReturn(expectedList);

    List<User> actual = new ArrayList<>(this.userService.searchUsers(SearchType.NAME, "test"));
    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () -> assertEquals(expectedList, actual));
  }

  @Test
  void shouldThrowErrorIfArgumentMismatch() {
    assertThrows(
        IllegalArgumentException.class,
        () -> this.userService.searchUsers(SearchType.TYPE, "not a type"));
  }

  @Test
  void shouldDeleteUser() {
    when(this.userRepository.findById(testId)).thenReturn(Optional.of(existing));

    var result = this.userService.deleteUser(testId);
    verify(this.userRepository).delete(user.capture());

    assertAll(
        () -> assertNotNull(result, "The returned user should not be null"),
        () -> assertEquals(existing, result, "The result should equal the expected value"));
  }

  @Test
  void deleteUserShouldThrowWhenUserIsNotFound() {
    when(this.userRepository.findById(testId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> this.userService.deleteUser(testId));
  }
}
