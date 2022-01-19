package com.teddycrane.racemanagement.test.integration.tests.users;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.test.integration.tests.IntegrationBase;
import com.teddycrane.racemanagement.test.integration.utils.JwtTokenProviderMock;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class getUser extends IntegrationBase {

  // todo make an abstract class to hold all of this
  private static UUID testId;

  @BeforeAll
  void setUp() {
    if (userRepository.findByUsername("testuser").isEmpty()) {
      testId = userRepository
                   .save(new User("Test", "User", "testuser", "email@email.com",
                                  "password"))
                   .getId();
    }
  }

  @Test
  void getUser() throws Exception {
    var result = this.mockMvc.perform(get("/user/" + testId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", String.format("%s %s", BEARER, JwtTokenProviderMock.generateMockToken("testuser")));

    assertAll(() -> assertNotNull(result, "The result should not be null"));
  }
}
