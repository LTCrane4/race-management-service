package com.teddycrane.racemanagement.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.security.util.TokenManager;
import com.teddycrane.racemanagement.services.UserService;
import com.teddycrane.racemanagement.services.UserServiceImpl;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

public class UserServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private TokenManager tokenManager;
  @Mock private AuthenticationManager authenticationManager;

  private UserService userService;

  private User existing;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    this.userService = new UserServiceImpl(
        this.userRepository, this.tokenManager, this.authenticationManager);
    this.existing = TestResourceGenerator.generateUser();
  }

  @Test
  public void userServiceShouldConstruct() {
    assertNotNull(this.userService);
  }

  @Test
  public void getUserShouldReturnUser() {
    when(this.userRepository.findById(any(UUID.class)))
        .thenReturn(Optional.of(TestResourceGenerator.generateUser()));

    Optional<User> result = this.userService.getUser(UUID.randomUUID());

    assertNotNull(result.get());
  }

  @Test
  public void createUserShouldCreate() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userRepository.save(any(User.class))).thenReturn(expected);

    User actual =
        this.userService.createUser("", "", "", "", "", UserType.USER);
    assertEquals(expected, actual);
  }

  @Test
  public void createUserShouldHandleDuplicates() {
    User existing = TestResourceGenerator.generateUser();
    when(this.userRepository.findByUsername(anyString()))
        .thenReturn(Optional.of(existing));

    assertThrows(
        DuplicateItemException.class,
        () -> this.userService.createUser("", "", "", "", "", UserType.USER));
  }

  @Test
  public void loginShouldReturnToken() throws Exception {
    when(this.userRepository.findByUsername(anyString()))
        .thenReturn(Optional.of(this.existing));

    AuthenticationResponse response =
        this.userService.login(existing.getUsername(), existing.getPassword());
    assertNotNull(response);
  }

  @Test
  public void loginShouldHandleUserNotFound() {
    when(this.userRepository.findByUsername(anyString()))
        .thenReturn(Optional.empty());

    assertThrows(NotAuthorizedException.class,
                 () -> this.userService.login("test", "test"));
  }

  @Test
  public void getAllUsersShouldReturnListOfUsers() {
    Collection<User> expectedList = TestResourceGenerator.generateUserList(5);
    when(this.userRepository.findAll()).thenReturn((List<User>)expectedList);

    Collection<User> result = this.userService.getAllUsers();

    assertNotNull(result);
  }
}
