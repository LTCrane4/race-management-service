package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.race.response.RaceCollectionResponse;
// import com.teddycrane.racemanagement.services.RaceService;
import com.teddycrane.racemanagement.services.RaceService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
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
}
