package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.race.response.RaceCollectionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/race")
public interface RaceApi {

  @GetMapping
  @Operation(description = "Get all races")
  @ApiResponse(responseCode = "200", description = "Found all races")
  ResponseEntity<RaceCollectionResponse> getAllRaces();
}
