package com.teddycrane.racemanagement.test.integration.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HealthCheck extends IntegrationBase {

  @Test
  void shouldStart() {
    assertTrue(CONTAINER.isRunning());
  }
}
