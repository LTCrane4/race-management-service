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

  // mapped from LocalTime.
  private String startTime;

  // mapped from LocalDate.
  private String startDate;
}
