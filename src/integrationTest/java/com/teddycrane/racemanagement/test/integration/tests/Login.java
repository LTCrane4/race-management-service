package com.teddycrane.racemanagement.test.integration.tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class Login extends IntegrationBase {
  public static final String LOGIN_ENDPOINT = "/login";

  @Test
  public void login() throws Exception {
    Gson gson = new Gson();
    AuthenticationRequest request = new AuthenticationRequest("test", "test");

    var response = mockMvc.perform(post(LOGIN_ENDPOINT)
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .content(gson.toJson(request)));

    assertAll(() -> assertNotNull(response, "The response should not be null"));
  }
}
