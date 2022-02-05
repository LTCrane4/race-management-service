package com.teddycrane.racemanagement.test.integration.tests.race;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
class GetRace extends RaceBase {

  @Test
  void getRace_shouldGetRace() throws Exception {
    this.mockMvc
        .perform(get(RACE_BASE_PATH + "/" + RACE_ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists());
  }
}
