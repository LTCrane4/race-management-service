package com.teddycrane.racemanagement.utils.mapper;

import com.teddycrane.racemanagement.model.race.RaceDTO;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseMapper {
  public static ResponseEntity<RaceDTO> createRaceResponse(RaceDTO race) {
    return ResponseEntity.ok(race);
  }
}
