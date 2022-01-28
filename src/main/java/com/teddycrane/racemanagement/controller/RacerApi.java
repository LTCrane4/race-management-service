package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.UpdateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/racer")
public interface RacerApi {

  @GetMapping
  @Operation(summary = "Get all racers")
  @ApiResponse(
      responseCode = "200",
      description = "Found racers",
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = RacerCollectionResponse.class))
      })
  ResponseEntity<RacerCollectionResponse> getAllRacers();

  @GetMapping("/{id}")
  @Operation(summary = "Get single racer")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found racer",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Racer.class))
            }),
        @ApiResponse(responseCode = "400", description = "Invalid racer id"),
        @ApiResponse(responseCode = "404", description = "No racer found")
      })
  ResponseEntity<Racer> getRacer(@PathVariable("id") String id);

  @PostMapping
  ResponseEntity<Racer> createRacer(@NonNull @Valid @RequestBody CreateRacerRequest request);

  @PatchMapping("/{id}")
  ResponseEntity<Racer> updateRacer(
      @PathVariable("id") String id, @Valid @RequestBody UpdateRacerRequest request);
}
