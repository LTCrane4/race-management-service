package com.teddycrane.racemanagement.model.user.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddycrane.racemanagement.enums.UserStatus;
import com.teddycrane.racemanagement.enums.UserType;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UserDTO {
  private final UUID id;
  private final Instant createdTimestamp;
  private String firstName, lastName;
  @Email private String email;
  private String username;
  private UserType userType;
  private UserStatus userStatus;
  private Instant updatedTimestamp;

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
