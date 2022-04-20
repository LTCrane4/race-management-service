package com.teddycrane.racemanagement.test.integration.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class HealthCheck extends IntegrationBase {

  @Test
  void shouldStart() throws Exception {
    assertTrue(CONTAINER.isRunning(), "The test Postgres container should be running");

    this.mockMvc.perform(get("/actuator/health")).andExpect(status().isOk());
  }
}
