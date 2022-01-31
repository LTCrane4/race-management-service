package com.teddycrane.racemanagement.test.integration.tests.users;

import static com.teddycrane.racemanagement.test.integration.constants.Constants.AUTHORIZATION_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
class CreateUser extends UserBase {

  @Test
  void createUserShouldCreate() throws Exception {
    JsonObject object = new JsonObject();
    object.addProperty("username", "testcreate");
    object.addProperty("password", "password");
    object.addProperty("firstName", "Test");
    object.addProperty("lastName", "User");
    object.addProperty("email", "test@fake.fake");

    this.mockMvc
        .perform(
            post(CREATE_USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.toString())
                .header(AUTHORIZATION_HEADER, USER_TOKEN))
        .andExpectAll(
            status().isOk(),
            jsonPath("$").exists(),
            jsonPath(USERNAME_PATH).value("testcreate"),
            jsonPath(FIRST_NAME_PATH).value("Test"),
            jsonPath("$.lastName").value("User"),
            jsonPath("$.email").value("test@fake.fake"),
            jsonPath("$.userType").value("USER"));
  }
}
