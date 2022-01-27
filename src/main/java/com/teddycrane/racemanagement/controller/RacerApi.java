package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/racer")
public interface RacerApi {

  @GetMapping
  ResponseEntity<RacerCollectionResponse> getAllRacers();
}
