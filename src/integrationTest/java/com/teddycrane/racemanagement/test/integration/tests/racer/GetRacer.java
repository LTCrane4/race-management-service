package com.teddycrane.racemanagement.test.integration.tests.racer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
class GetRacer extends RacerBase {

  @Test
  void shouldGetAllRacers() throws Exception {
    this.mockMvc
        .perform(
            get(this.RACER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, USER_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpectAll(jsonPath("$.racers").isNotEmpty(), jsonPath("$.racers").isArray());
  }

  @Test
  void shouldGetSingleRacer() throws Exception {
    this.mockMvc
        .perform(
            get(this.RACER_PATH + "/" + this.TEST_RACER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER, USER_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty());
  }
}
