package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.model.race.Race;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface RaceService {
  List<Race> getAllRaces();

  Race getRace(UUID id);

  Race createRace(String name, Category category, List<UUID> racerIds) throws ConflictException;

  Race addRacerToRace(UUID raceId, List<UUID> racerIds, Instant updatedTimestamp);
}
