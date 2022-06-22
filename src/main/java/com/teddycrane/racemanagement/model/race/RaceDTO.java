package com.teddycrane.racemanagement.model.race;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.model.racer.Racer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class RaceDTO {

  private UUID id;
  private Instant createdTimestamp;
  private Instant updatedTimestamp;
  private String name;
  private Category category;
  private Collection<Racer> racers;
  private LocalDate eventDate;
  private LocalTime startTime;
  private LocalTime finishTime;

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
