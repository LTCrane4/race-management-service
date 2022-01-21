package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.services.RacerService;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/racer")
public class RacerController extends BaseController {

  private final RacerService racerService;

  public RacerController(RacerService racerService) {
    super();
    this.racerService = racerService;
  }

  @GetMapping
  public RacerCollectionResponse getAllRacers() {
    logger.info("getAllRacers called");

    return new RacerCollectionResponse(this.racerService.getAllRacers());
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
}
