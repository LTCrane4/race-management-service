package com.teddycrane.racemanagement.unittest;

import com.teddycrane.racemanagement.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

  private User user;
  
  @BeforeEach
  public void setUp() {
    user = new User();
  }

  @Test
  public void user_shouldConstruct() {
    assertNotNull(user);
  }
}
