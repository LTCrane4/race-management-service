package com.teddycrane.racemanagement.test.integration.tests.users;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
class GetUser extends UserBase {

  @Test
  void getUser_shouldReturnValidUser() throws Exception {
    this.mockMvc
        .perform(
            get("/user/" + USER_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION_HEADER, USER_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists())
        .andExpect(jsonPath("$.username").value("testuser"));
  }

  @Test
  void getUser_ShouldReturn404WhenNoUserIsFound() throws Exception {
    this.mockMvc
        .perform(
            get("/user/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, USER_TOKEN))
        .andExpect(status().isNotFound());
  }
}
