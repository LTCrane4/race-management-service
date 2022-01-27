package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.services.RacerService;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RacerController extends BaseController implements RacerApi {

  private final RacerService racerService;

  public RacerController(RacerService racerService) {
    super();
    this.racerService = racerService;
  }

  @GetMapping
  public ResponseEntity<RacerCollectionResponse> getAllRacers() {
    logger.info("getAllRacers called");

    return ResponseEntity.ok(new RacerCollectionResponse(this.racerService.getAllRacers()));
  }

  @GetMapping("/{id}")
  public Racer getRacer(@PathVariable String id) throws BadRequestException, NotFoundException {
    logger.info("getRacer called");

    try {
      UUID userId = UUID.fromString(id);
      return this.racerService.getRacer(userId);
    } catch (IllegalArgumentException e) {
      logger.error("Could not parse a valid UUID from the provided id");
      throw new BadRequestException("Invalid user id provided");
    }
  }

  @PostMapping
  public Racer createRacer(@NonNull @RequestBody @Valid CreateRacerRequest request) {
    logger.info("createRacer called");

    return this.racerService.createRacer(
        request.getFirstName(),
        request.getLastName(),
        request.getCategory(),
        request.getMiddleName(),
        request.getTeamName(),
        request.getPhoneNumber(),
        request.getEmail(),
        request.getBibNumber());
  }
}
