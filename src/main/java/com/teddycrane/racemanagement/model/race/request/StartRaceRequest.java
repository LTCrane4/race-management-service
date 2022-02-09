package com.teddycrane.racemanagement.model.race.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StartRaceRequest {
  @NotBlank(message = "Updated timestamp must be included")
  private String updatedTimestamp;

  // Desired race start time, must not be in the past.  Maps to LocalTime.
  private String startTime;
  // Desired start date, must not be in the past. Maps to LocalDate.
  private String startDate;
}
