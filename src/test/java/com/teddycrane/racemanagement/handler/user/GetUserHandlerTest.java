package com.teddycrane.racemanagement.handler.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserHandlerTest {

  @Mock private UserRepository userRepository;

  private Handler<UUID, User> getUserHandler;

  @BeforeEach
  void setUp() {
    this.getUserHandler = new GetUserHandler(this.userRepository);
  }

  @Test
  void shouldReturnValidUser() {
    User expected = TestResourceGenerator.generateUser();
    UUID testId = UUID.randomUUID();
    when(this.userRepository.findById(testId)).thenReturn(Optional.of(expected));

    var result = this.getUserHandler.resolve(testId);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(expected, result, "The result should equal the expected value"));
  }

  @Test
  void shouldReturnNullIfNoUserFound() {
    UUID testId = UUID.randomUUID();
    when(this.userRepository.findById(testId)).thenReturn(Optional.empty());

    var result = this.getUserHandler.resolve(testId);

    assertNull(result, "The result should be null if no user is found for the given id");
  }
}
