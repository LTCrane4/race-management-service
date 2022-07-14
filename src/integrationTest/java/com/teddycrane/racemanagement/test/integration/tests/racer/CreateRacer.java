package com.teddycrane.racemanagement.test.integration.tests.racer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CreateRacer extends RacerBase {

  @Test
  void shouldCreateRacer() throws Exception {
    var body =
        CreateRacerRequest.builder()
            .firstName("Test")
            .lastName("Racer")
            .category(Category.CAT1.toString())
            .build();

    this.mockMvc
        .perform(
            post(this.RACER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, USER_TOKEN)
                .content(this.mapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$").exists(),
            jsonPath("$.firstName").value("Test"),
            jsonPath("$.lastName").value("Racer"));
  }
}
