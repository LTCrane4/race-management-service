package com.teddycrane.racemanagement.test.integration.tests;

import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.services.AuthenticationService;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class IntegrationBase {
  protected static final String AUTHORIZATION_HEADER = "Authorization";
  protected static final String BEARER = "Bearer";

  @Autowired protected UserRepository userRepository;

  // required to get tests running because it won't construct otherwise
  @MockBean private AuthenticationService authService;

  @Container
  public static final MySQLContainer<?> CONTAINER =
      new MySQLContainer<>("mysql:8.0.20")
          .withDatabaseName("test")
          .withUsername("tester")
          .withPassword("password")
          .waitingFor(Wait.defaultWaitStrategy());

  @Autowired protected MockMvc mockMvc;

  @BeforeAll
  public static void setUpDatabase() {
    CONTAINER.start();
  }
}
