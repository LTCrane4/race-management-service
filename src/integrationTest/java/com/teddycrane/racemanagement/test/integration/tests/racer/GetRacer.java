package com.teddycrane.racemanagement.test.integration.tests.racer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GetRacer extends RacerBase {

  @Test
  void shouldGetRacer() {
    this.mockMvc.perform(get());
  }
}
