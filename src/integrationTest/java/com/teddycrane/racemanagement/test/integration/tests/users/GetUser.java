package com.teddycrane.racemanagement.test.integration.tests.users;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetUser extends UserBase {

  @Test
  void getUser_shouldReturnValidUser() throws Exception {
    this.mockMvc
        .perform(
            get("/user/" + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, USER_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists())
        .andExpect(jsonPath("$.username").value("testuser"));
  }
}
