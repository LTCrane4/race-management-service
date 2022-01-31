package com.teddycrane.racemanagement.handler.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.handler.user.request.UpdateUserHandlerRequest;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserHandlerTest {

  private final UUID testId = UUID.randomUUID();
  private final User existingUser = TestResourceGenerator.generateUser();
  @Mock private UserRepository userRepository;
  @Captor private ArgumentCaptor<User> userCaptor;
  private Handler<UpdateUserHandlerRequest, User> updateUserHandler;

  @BeforeEach
  void setUp() {
    this.updateUserHandler = new UpdateUserHandler(this.userRepository);
  }

  @Test
  void shouldUpdateAllParams() {
    when(this.userRepository.findById(testId)).thenReturn(Optional.of(existingUser));
    when(this.userRepository.save(any(User.class)))
        .thenAnswer((arguments) -> arguments.getArgument(0));

    UpdateUserHandlerRequest request =
        new UpdateUserHandlerRequest(
            new UpdateUserRequest(
                "fname",
                "lname",
                "email@email.test",
                UserType.ADMIN,
                existingUser.getUpdatedTimestamp().toString()),
            testId);

    var actual = this.updateUserHandler.resolve(request);
    verify(this.userRepository).save(userCaptor.capture());

    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () ->
            assertEquals(
                userCaptor.getValue(),
                actual,
                "The result should match the input to the database"));
  }

  @Test
  void shouldUpdateNoParams() {
    when(this.userRepository.findById(testId)).thenReturn(Optional.of(existingUser));
    when(this.userRepository.save(any(User.class)))
        .thenAnswer((arguments) -> arguments.getArgument(0));
    UpdateUserHandlerRequest request =
        new UpdateUserHandlerRequest(new UpdateUserRequest(null, null, null, null, null), testId);

    var result = this.updateUserHandler.resolve(request);
    verify(this.userRepository).save(userCaptor.capture());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                existingUser, result, "The user should not update if all the parameters are null"),
        () ->
            assertEquals(
                existingUser,
                userCaptor.getValue(),
                "The user sent to the database should not be updated"));
  }

  @Test
  void shouldThrowExceptionIfUserIsNotFound() {
    when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    UpdateUserHandlerRequest request = new UpdateUserHandlerRequest();
    request.setRequesterId(UUID.randomUUID());

    assertThrows(NotFoundException.class, () -> this.updateUserHandler.resolve(request));
  }
}
