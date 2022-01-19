package com.teddycrane.racemanagement.test.integration.tests;

import com.teddycrane.racemanagement.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = Application.class)
@Testcontainers
public class IntegrationBase {
  protected static final String AUTHORIZATION_HEADER = "Authorization";
  protected static final String BEARER = "Bearer";

  @Container
  protected static final MySQLContainer CONTAINER =
      new MySQLContainer<>(DockerImageName.parse("mysql:8.0.20"))
          .withDatabaseName("testDb")
          .withUsername("tester")
          .withPassword("password")
          .waitingFor(Wait.defaultWaitStrategy());

  @Autowired protected MockMvc mockMvc;
}
