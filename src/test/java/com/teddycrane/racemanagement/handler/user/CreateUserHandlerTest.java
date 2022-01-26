package com.teddycrane.racemanagement.handler.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserHandlerTest {

  private Handler<CreateUserRequest, User> createUserHandler;

  @Mock private UserRepository userRepository;
  @Captor private ArgumentCaptor<User> userCaptor;

  @BeforeEach
  void setUp() {
    this.createUserHandler = new CreateUserHandler(userRepository);
  }

  @Test
  void shouldCreateUser() {
    when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(this.userRepository.save(any(User.class)))
        .thenAnswer((arguments) -> arguments.getArgument(0));

    var result =
        this.createUserHandler.resolve(
            new CreateUserRequest(
                "username", "password", "fname", "lname", "email@email.com", UserType.USER));

    verify(this.userRepository).save(userCaptor.capture());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                result, userCaptor.getValue(), "The saved item and the result should be equal"));
  }

  @Test
  void shouldThrowWhenUserAlreadyExists() {
    when(this.userRepository.findByUsername(anyString()))
        .thenReturn(Optional.of(TestResourceGenerator.generateUser()));

    assertThrows(
        DuplicateItemException.class,
        () ->
            this.createUserHandler.resolve(
                new CreateUserRequest("", "", "", "", "", UserType.USER)));
  }
}
