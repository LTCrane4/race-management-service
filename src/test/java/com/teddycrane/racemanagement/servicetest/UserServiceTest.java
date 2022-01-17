package com.teddycrane.racemanagement.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.services.UserService;
import com.teddycrane.racemanagement.services.UserServiceImpl;
import com.teddycrane.racemanagement.util.TokenManager;
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

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    this.userService = new UserServiceImpl(
        this.userRepository, this.tokenManager, this.authenticationManager);
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
}
