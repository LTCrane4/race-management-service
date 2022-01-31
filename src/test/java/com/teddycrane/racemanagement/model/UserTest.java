package com.teddycrane.racemanagement.model;

import static org.junit.jupiter.api.Assertions.*;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
  }

  @Test
  void user_shouldConstruct() {
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

  @Test
  void shouldEqual() {
    User expected = TestResourceGenerator.generateUser();
    User result = new User(expected);

    assertEquals(expected, result);
  }

  @Test
  void shouldCaptureAllEqualsBranches() {
    User expected = TestResourceGenerator.generateUser();
    User actual = new User(expected);

    assertEquals(expected, actual);

    actual.setUserType(UserType.ADMIN);
    assertNotEquals(expected, actual);

    actual.setEmail("test");
    assertNotEquals(expected, actual);

    actual.setPassword("test");
    assertNotEquals(expected, actual);

    actual.setUsername("test");
    assertNotEquals(expected, actual);

    actual.setLastName("lastName");
    assertNotEquals(expected, actual);

    actual.setFirstName("firstName");
    assertNotEquals(expected, actual);
    assertNotEquals(expected, new User());
  }

  // TODO get email validation working again
  @Test
  void setEmailShouldValidateEmail() {
    user.setEmail("test@test.fake");
    assertEquals("test@test.fake", user.getEmail());
  }
}
