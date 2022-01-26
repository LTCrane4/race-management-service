package com.teddycrane.racemanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

@ExtendWith(MockitoExtension.class)
@Profile("local")
class AuthenticationServiceTest {

  @Mock private UserRepository userRepository;

  private AuthenticationService authenticationService;

  @BeforeEach
  void init() {
    this.authenticationService = new AuthenticationService(this.userRepository);
  }

  @Test
  void loadsByUserName() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userRepository.findOneByUsername(anyString())).thenReturn(expected);

    UserPrincipal actual = (UserPrincipal) this.authenticationService.loadUserByUsername("test");

    assertEquals(expected, actual.getUser());
  }

  @Test
  void initializeShouldCallMethods() {
    this.authenticationService.initialize();
  }
}
