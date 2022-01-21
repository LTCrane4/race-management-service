package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.services.RacerService;
import org.springframework.web.bind.annotation.GetMapping;
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
}
