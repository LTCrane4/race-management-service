package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.race.response.RaceCollectionResponse;
// import com.teddycrane.racemanagement.services.RaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RaceController extends BaseController implements RaceApi {

  // private final RaceService raceService;

  public RaceController() {
    super();
  }

  @Override
  public ResponseEntity<RaceCollectionResponse> getAllRaces() {
    return null;
  }
}
