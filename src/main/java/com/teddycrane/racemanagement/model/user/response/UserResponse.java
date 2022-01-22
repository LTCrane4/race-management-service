package com.teddycrane.racemanagement.model.user.response;

import com.google.gson.Gson;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.user.User;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * This class is used for responses from the User Controller to obfuscate sensitive information at
 * the client level
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserResponse {

  private UUID id;

  private String firstName, lastName, email, username;

  private UserType userType;

  public UserResponse(@NonNull User u) {
    this(
        u.getId(),
        u.getFirstName(),
        u.getLastName(),
        u.getEmail(),
        u.getUsername(),
        u.getUserType());
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
