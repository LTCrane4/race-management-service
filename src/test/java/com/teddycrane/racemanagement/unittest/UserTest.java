package com.teddycrane.racemanagement.unittest;

import static org.junit.jupiter.api.Assertions.*;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
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

  @Test
  public void shouldEqual() {
    User expected = TestResourceGenerator.generateUser();
    User result = new User(expected);

    assertEquals(expected, result);
  }

  @Test
  public void shouldRepresentString() {
    assertNotNull(TestResourceGenerator.generateUser().toString());
  }

  @Test
  public void shouldCaptureAllEqualsBranches() {
    User expected = TestResourceGenerator.generateUser();
    User actual = new User(expected);

    assertEquals(expected, actual);

    actual.setUserType(UserType.ADMIN);
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

  @Test
  public void setEmailShouldValidateEmail() {
    user.setEmail("test@test.fake");
    assertEquals("test@test.fake", user.getEmail());

    user.setEmail("test");
    assertNotEquals("test", user.getEmail());
  }
}
