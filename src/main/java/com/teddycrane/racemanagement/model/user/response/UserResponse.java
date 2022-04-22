package com.teddycrane.racemanagement.model.user.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddycrane.racemanagement.enums.UserStatus;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.user.User;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.NotNull;
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
@Deprecated
public class UserResponse implements Response {

  @NotNull(message = "The User ID can never be null")
  private UUID id;

  private String firstName, lastName, email, username;

  private UserType userType;

  private Instant createdTimestamp, updatedTimestamp;

  private UserStatus status;

  public UserResponse(@NonNull User u) {
    this(
        u.getId(),
        u.getFirstName(),
        u.getLastName(),
        u.getEmail(),
        u.getUsername(),
        u.getUserType(),
        u.getCreatedTimestamp(),
        u.getUpdatedTimestamp(),
        u.getStatus());
  }

  @Override
  public String toString() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "";
    }
  }
}
