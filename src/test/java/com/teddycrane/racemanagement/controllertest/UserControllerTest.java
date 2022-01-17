package com.teddycrane.racemanagement.controllertest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teddycrane.racemanagement.controller.UserController;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.model.request.CreateUserRequest;
import com.teddycrane.racemanagement.services.UserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest {

  private UserController userController;

  @Mock private UserService userService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    this.userController = new UserController(this.userService);
  }

  @Test
  public void userController_shouldConstruct() {
    assertNotNull(userController);
  }

  @Test
  public void getUser_shouldReturnUser() {
    when(this.userService.getUser(any(UUID.class))).thenReturn(new User());
    User result = this.userController.getUser(UUID.randomUUID().toString());
    assertNotNull(result);
  }

  @Test
  public void getUser_shouldThrowBadRequestErrorIfBadId() {
    assertThrows(BadRequestException.class,
                 () -> this.userController.getUser("test"));
  }

  @Test
  public void createUserShouldCreateAUser() {
    User expected = TestResourceGenerator.generateUser();
    when(this.userService.createUser(anyString(), anyString(), anyString(),
                                     anyString(), anyString(),
                                     any(UserType.class)))
        .thenReturn(expected);

    User result = this.userController.createUser(
        new CreateUserRequest("", "", "", "", "", UserType.USER));
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void createUserShouldHandleServiceErrors() {
    when(this.userService.getUser(any(UUID.class)))
        .thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class,
        () -> this.userController.getUser(UUID.randomUUID().toString()));
  }
}
