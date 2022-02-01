package com.teddycrane.racemanagement.test.integration.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.security.util.TokenManager;
import com.teddycrane.racemanagement.services.AuthenticationService;
import com.teddycrane.racemanagement.test.integration.utils.JwtTokenProviderMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser("testuser")
public abstract class IntegrationBase {

  @Container public static final MySQLContainer<?> CONTAINER;

  static {
    CONTAINER =
        new MySQLContainer<>("mysql:8.0.20")
            .withReuse(true)
            .withDatabaseName("test")
            .withUsername("tester")
            .withPassword("password")
            .waitingFor(Wait.forLogMessage("mysqld: ready for connections", 1));
    CONTAINER.start();
  }

  protected static final String AUTHORIZATION_HEADER = "Authorization";
  protected static final String BEARER = "Bearer";
  protected static final String USER_TOKEN =
      String.format("%s %s", BEARER, JwtTokenProviderMock.generateMockToken("testuser"));

  @Autowired protected UserRepository userRepository;

  @Autowired protected MockMvc mockMvc;

  // required to get tests running because it won't construct otherwise
  @MockBean private AuthenticationService authService;

  @MockBean private TokenManager tokenManager;

  public static void setUpDatabase() {
    CONTAINER.start();
  }

  @BeforeAll
  public static void setUpTestSuite() {
    // only start new container if it isn't already running
    if (!CONTAINER.isRunning()) {
      setUpDatabase();
    }
  }

  private void setUpMockTokenManager() {
    when(this.tokenManager.validateToken(anyString(), any(UserDetails.class))).thenReturn(true);
    // hard code token manager to return the test user
    when(this.tokenManager.getUsernameFromToken(anyString())).thenReturn("testuser");
  }

  @BeforeEach
  void setUp() {
    setUpMockTokenManager();
  }
}
