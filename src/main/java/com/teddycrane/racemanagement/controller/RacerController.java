package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.services.RacerService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RacerController extends BaseController implements RacerApi {

  private final RacerService racerService;

  public RacerController(RacerService racerService) {
    super();
    this.racerService = racerService;
  }

  public ResponseEntity<RacerCollectionResponse> getAllRacers() {
    logger.info("getAllRacers called");

    return ResponseEntity.ok(new RacerCollectionResponse(this.racerService.getAllRacers()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Racer> getRacer(String id) throws BadRequestException, NotFoundException {
    logger.info("getRacer called");

    try {
      UUID userId = UUID.fromString(id);
      return ResponseEntity.ok(this.racerService.getRacer(userId));
    } catch (IllegalArgumentException e) {
      logger.error("Could not parse a valid UUID from the provided id");
      return ResponseEntity.badRequest().build();
    } catch (NotFoundException e) {
      logger.error("No racer found for the id {}", id);
      return ResponseEntity.notFound().build();
    }
  }

  public ResponseEntity<Racer> createRacer(@NonNull CreateRacerRequest request) {
    logger.info("createRacer called");

    try {
      return ResponseEntity.ok(
          this.racerService.createRacer(
              request.getFirstName(),
              request.getLastName(),
              request.getCategory(),
              request.getMiddleName(),
              request.getTeamName(),
              request.getPhoneNumber(),
              request.getEmail(),
              request.getBibNumber()));
    } catch (DuplicateItemException e) {
      logger.error("Cannot create a duplicate of an existing item");
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
