package com.teddycrane.racemanagement.model.racer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddycrane.racemanagement.enums.Category;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RacerDTO {
  private UUID id;
  private Instant createdTimestamp;
  private Instant updatedTimestamp;
  private String firstName, middleName, lastName, teamName, phoneNumber;
  @Email private String email;
  private Category category;
  private String bibNumber;
  private boolean isDeleted;

  @Generated
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
