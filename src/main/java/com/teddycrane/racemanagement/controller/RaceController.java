package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.race.request.CreateRaceRequest;
import com.teddycrane.racemanagement.model.race.response.RaceCollectionResponse;
// import com.teddycrane.racemanagement.services.RaceService;
import com.teddycrane.racemanagement.services.RaceService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RaceController extends BaseController implements RaceApi {

  private final RaceService raceService;

  public RaceController(RaceService raceService) {
    super();
    this.raceService = raceService;
  }

  @Override
  public ResponseEntity<RaceCollectionResponse> getAllRaces() {
    logger.info("getAllRaces called");

    return ResponseEntity.ok(new RaceCollectionResponse(this.raceService.getAllRaces()));
  }

  @Override
  public ResponseEntity<Race> getRace(String id) {
    logger.info("getRace called");

    try {
      UUID raceId = UUID.fromString(id);
      return ResponseEntity.ok(this.raceService.getRace(raceId));
    } catch (IllegalArgumentException e) {
      logger.error("Invalid UUID provided");
      return ResponseEntity.badRequest().build();
    } catch (NotFoundException e) {
      logger.error("No race found for the id {}", id);
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  public ResponseEntity<Race> createRace(@NonNull CreateRaceRequest request) {
    logger.info("createRace called");
    String name = request.getName();
    Category category = request.getCategory();
    List<UUID> racers = List.of();

    if (request.getRacerIds() != null) {
      // ensure list elements are not null
      racers = request.getRacerIds().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    try {
      return ResponseEntity.ok(this.raceService.createRace(name, category, racers));
    } catch (ConflictException e) {
      logger.error("Name Collision: Cannot create duplicate race");
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
