package com.teddycrane.racemanagement.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.services.UserService;
import com.teddycrane.racemanagement.services.UserServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {
  @Mock private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    this.userService = new UserServiceImpl(this.userRepository);
  }

  @Test
  public void userServiceShouldConstruct() {
    assertNotNull(this.userService);
  }

  @Test
  public void getUserShouldReturnUser() {
    when(this.userRepository.findById(any(UUID.class)))
        .thenReturn(Optional.of(TestResourceGenerator.generateUser()));

    User result = this.userService.getUser(UUID.randomUUID());

    assertNotNull(result);
  }
}
