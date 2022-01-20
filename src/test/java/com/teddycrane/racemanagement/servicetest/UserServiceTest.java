package com.teddycrane.racemanagement.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

public class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private TokenManager tokenManager;
  @Mock private AuthenticationManager authenticationManager;

  private UUID testId;

  private UserService userService;

  private User existing;

  @Captor private ArgumentCaptor<User> user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.userService =
        new UserServiceImpl(this.userRepository, this.tokenManager, this.authenticationManager);
    this.existing = TestResourceGenerator.generateUser();
    this.testId = UUID.randomUUID();
  }

  @Test
  void userServiceShouldConstruct() {
    assertNotNull(this.userService);
  }

  @Test
  void getUserShouldReturnUser() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.of(expected));

    Optional<User> result = this.userService.getUser(UUID.randomUUID());

    assertAll(
        () -> assertTrue(result.isPresent(), "The result should not be null"),
        () ->
            assertEquals(
                expected,
                result.orElse(new User()),
                "The actual value should equal the expected one"));
  }

  @Test
  void createUserShouldCreate() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userRepository.save(any(User.class))).thenReturn(expected);

    User actual = this.userService.createUser("", "", "", "", "", UserType.USER);
    assertEquals(expected, actual);
  }

  @Test
  public void createUserShouldCreateWithNoType() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userRepository.save(any(User.class))).thenReturn(expected);

    User actual = this.userService.createUser("", "", "", "", "", null);
    assertEquals(expected, actual);
    assertEquals("user", actual.getUserType().toString());
  }

  @Test
  void createUserShouldHandleDuplicates() {
    User existing = TestResourceGenerator.generateUser();
    when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.of(existing));

    assertThrows(
        DuplicateItemException.class,
        () -> this.userService.createUser("", "", "", "", "", UserType.USER));
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
    when(this.userRepository.findAll()).thenReturn((List<User>) expectedList);

    Collection<User> result = this.userService.getAllUsers();

    assertNotNull(result);
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
}
