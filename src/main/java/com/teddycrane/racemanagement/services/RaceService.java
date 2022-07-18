package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.race.request.SearchRaceRequest;
import java.time.Instant;
// import java.time.LocalDate;
// import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public interface RaceService {

  List<Race> getAllRaces();

  Race getRace(UUID id);

  Race createRace(String name, Category category, List<UUID> racerIds) throws ConflictException;

  Race addRacersToRace(UUID raceId, List<UUID> racerIds, Instant updatedTimestamp)
      throws ConflictException, NotFoundException;

  Race updateRace(UUID id, String name, Category category, Instant updatedTimestamp)
      throws ConflictException, NotFoundException;

  /**
   * Finds races that a racer is involved in
   *
   * @param racerId The UUID of a racer to find race involvment for
   * @return A List of Races that a racer is involved in
   * @throws NotFoundException Throws if the racer cannot be found
   */
  List<Race> getRacesForRacer(UUID racerId) throws NotFoundException;

  Race startRace(UUID raceId, Instant updatedTimestamp) throws ConflictException, NotFoundException;

  // should throw date exception too, once i get around to it
  // Race startRace(UUID raceId, Instant updatedTimestamp, LocalTime startTime, LocalDate startDate)
  // throws ConflictException, NotFoundException;

  boolean deleteRace(UUID raceId) throws NotFoundException;

  Collection<Race> searchRaces(@Validated SearchRaceRequest request);
}
