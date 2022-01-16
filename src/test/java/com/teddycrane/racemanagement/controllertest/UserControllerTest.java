package com.teddycrane.racemanagement.controllertest;

import static org.junit.jupiter.api.Assertions.*;

import com.teddycrane.racemanagement.controller.UserController;
import com.teddycrane.racemanagement.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserControllerTest {

  private UserController userController;

  @BeforeEach
  public void init() {
    this.userController = new UserController();
  }

  @Test
  public void userController_shouldConstruct() {
    assertNotNull(userController);
  }

  @Test
  public void getUser_shouldReturnUser() {
    User result = this.userController.getUser("test");
    assertNotNull(result);
  }
}
