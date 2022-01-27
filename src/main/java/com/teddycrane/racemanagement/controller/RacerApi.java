package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/racer")
public interface RacerApi {

  @GetMapping
  ResponseEntity<RacerCollectionResponse> getAllRacers();

  @GetMapping("/{id}")
  ResponseEntity<Racer> getRacer(@PathVariable("id") String id);

  @PostMapping
  ResponseEntity<Racer> createRacer(@NonNull @Valid @RequestBody CreateRacerRequest request);
}
