package com.teddycrane.racemanagement.controllertest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teddycrane.racemanagement.controller.UserController;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.model.User;
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
}
