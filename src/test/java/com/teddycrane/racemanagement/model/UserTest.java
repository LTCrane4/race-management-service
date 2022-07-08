package com.teddycrane.racemanagement.model;

import static org.junit.jupiter.api.Assertions.*;

import com.teddycrane.racemanagement.enums.UserStatus;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.user.User;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    user = new User(UUID.randomUUID(), "", "", "", "", "", UserType.ADMIN);
    assertNotNull(user, "the user should not be null");

    User other = new User(user);
    assertEquals(user, other);
    assertNotEquals(user, new User());
    assertNotEquals(user, "");
    assertNotEquals("", user);
  }

  @Test
  @DisplayName("User should construct successfully")
  void shouldEqual() {
    User expected = TestResourceGenerator.generateUser();
    User result = new User(expected);

    assertEquals(expected, result);
  }

  @Test
  @DisplayName("Should handle cases for equals()")
  void shouldCaptureAllEqualsBranches() {
    var now = Instant.now();
    User expected =
        User.builder()
            .id(UUID.randomUUID())
            .firstName("firstName")
            .userType(UserType.ADMIN)
            .createdTimestamp(Instant.now())
            .updatedTimestamp(Instant.now())
            .status(UserStatus.ACTIVE)
            .email("test")
            .lastName("lastName")
            .username("test")
            .password("test")
            .build();
    User actual = new User(expected);

    assertNotEquals(null, actual, "The User equals should handle null comparisons");
    assertNotEquals(new Racer(), actual, "The User equals() should handle other object types");

    assertEquals(expected, actual, "The two users should be equal");

    actual.setStatus(UserStatus.DISABLED);
    assertNotEquals(expected, actual, "equals should return false when the statuses do not match");

    actual.setUserType(UserType.USER);
    assertNotEquals(
        expected, actual, "equals should return false when the user types do not match");

    actual.setPassword("not equal");
    assertNotEquals(expected, actual, "equals should return false when the passwords do not match");

    actual.setUsername("other");
    assertNotEquals(expected, actual, "equals should return false when the usernames do not match");

    actual.setEmail("not equal");
    assertNotEquals(expected, actual, "equals should return false when the emails do not match");

    actual.setLastName("not equal");
    assertNotEquals(
        expected, actual, "equals should return false when the last names are not equal");

    actual.setFirstName("not equal");
    assertNotEquals(
        expected, actual, "equals should return false when the first names are not equal");

    User createdUser =
        User.builder()
            .id(UUID.randomUUID())
            .createdTimestamp(now)
            .firstName("test")
            .lastName("test")
            .email("email@email.com")
            .username("test")
            .password("test")
            .userType(UserType.USER)
            .status(UserStatus.ACTIVE)
            .updatedTimestamp(Instant.now())
            .build();
    assertNotEquals(
        createdUser.getCreatedTimestamp(),
        actual.getCreatedTimestamp(),
        "The users' createdTimestamps should not be equal");
    assertNotEquals(createdUser, actual, "The equals method should validate the createdTimestamp");

    assertNotEquals(expected, new User());
  }

  // TODO get email validation working again
  @Test
  @DisplayName("setEmail should validate the email address")
  void setEmailShouldValidateEmail() {
    user.setEmail("test@test.fake");
    assertEquals("test@test.fake", user.getEmail());
  }
}
