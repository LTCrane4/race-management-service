package com.teddycrane.racemanagement.unittest;

import static org.junit.jupiter.api.Assertions.*;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

  private User user;

  @BeforeEach
  public void setUp() {
    user = new User();
  }

  @Test
  public void user_shouldConstruct() {
    assertNotNull(user);

    user = new User("name", "name", "name", "name", "name");
    assertNotNull(user);

    user = new User("name", "name", "name", "name", "name", UserType.ADMIN);
    assertNotNull(user);

    User other = new User(user);
    assertEquals(user, other);
    assertNotEquals(user, new User());
    assertNotEquals(user, "");
    assertNotEquals("", user);
  }
}
