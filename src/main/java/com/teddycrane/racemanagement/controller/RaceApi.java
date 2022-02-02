package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.race.request.AddRacersRequest;
import com.teddycrane.racemanagement.model.race.request.CreateRaceRequest;
import com.teddycrane.racemanagement.model.race.response.RaceCollectionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/race")
public interface RaceApi {

  @GetMapping
  @Operation(description = "Get all races")
  @ApiResponse(responseCode = "200", description = "Found all races")
  ResponseEntity<RaceCollectionResponse> getAllRaces();

  @GetMapping("/{id}")
  @Operation(description = "Get single race")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully found race",
            content = {@Content(schema = @Schema(implementation = Race.class))}),
        @ApiResponse(responseCode = "404", description = "No race found"),
        @ApiResponse(responseCode = "400", description = "Invalid id provided")
      })
  ResponseEntity<Race> getRace(@PathVariable("id") String id);

  @PostMapping
  @Operation(description = "Create new race")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully created Race",
            content = {@Content(schema = @Schema(implementation = Race.class))}),
        @ApiResponse(
            responseCode = "409",
            description = "Could not create race: A race with the same name already exists")
      })
  ResponseEntity<Race> createRace(@Valid @RequestBody CreateRaceRequest request);

  @PostMapping("/{raceId}/add-racers")
  @Operation(description = "Add racers to existing race")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully added Racers",
            content = {@Content(schema = @Schema(implementation = Race.class))}),
        @ApiResponse(responseCode = "404", description = "No race found for the id")
      })
  ResponseEntity<Race> addRacersToRace(
      @PathVariable String raceId, @Valid @RequestBody AddRacersRequest request);
}
