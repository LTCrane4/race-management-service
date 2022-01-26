package com.teddycrane.racemanagement.handler.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUsersHandlerTest {

  private Handler<String, Collection<User>> getUsersHandler;

  @Mock private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    this.getUsersHandler = new GetUsersHandler(this.userRepository);
  }

  @Test
  void getUsersHandlerShouldReturnListOfUsers() {
    Collection<User> expected = TestResourceGenerator.generateUserList(5);
    when(this.userRepository.findAll()).thenReturn((List<User>) expected);

    var result = this.getUsersHandler.resolve("");

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(expected, result, "The result should match the expected value"));
  }
}
