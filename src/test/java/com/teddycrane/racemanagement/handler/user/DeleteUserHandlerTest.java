package com.teddycrane.racemanagement.handler.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.handler.user.request.DeleteUserRequest;
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
class DeleteUserHandlerTest {

  private Handler<DeleteUserRequest, User> deleteUserHandler;

  @Mock private UserRepository userRepository;

  private final UUID testId = UUID.randomUUID();
  private final User existing = TestResourceGenerator.generateUser();

  @BeforeEach
  void setUp() {
    this.deleteUserHandler = new DeleteUserHandler(this.userRepository);
  }

  @Test
  void shouldDeleteUser() {
    when(this.userRepository.findById(testId)).thenReturn(Optional.of(existing));

    var result = this.deleteUserHandler.resolve(new DeleteUserRequest(testId));
    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(existing, result, "The result should match the expected value"));
  }

  @Test
  void shouldNotDeleteIfNotFound() {
    when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> this.deleteUserHandler.resolve(new DeleteUserRequest(UUID.randomUUID())));
  }
}
