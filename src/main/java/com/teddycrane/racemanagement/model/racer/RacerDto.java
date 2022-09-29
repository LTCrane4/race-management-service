package com.teddycrane.racemanagement.model.racer;

import com.teddycrane.racemanagement.enums.Category;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RacerDto implements Serializable {
  private UUID id;
  private Instant createdTimestamp;
  private Instant updatedTimestamp;
  private String firstName, middleName, lastName, teamName, phoneNumber;
  @Email private String email;
  private Category category;
  private String bibNumber;
  private boolean isDeleted;
}
