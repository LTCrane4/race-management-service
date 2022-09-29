package com.teddycrane.racemanagement.utils.mapper;

import com.teddycrane.racemanagement.model.race.RaceDto;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseMapper {
  public static ResponseEntity<RaceDto> createRaceResponse(RaceDto race) {
    return ResponseEntity.ok(race);
  }
}
