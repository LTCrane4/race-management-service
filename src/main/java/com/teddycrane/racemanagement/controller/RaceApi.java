package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.race.request.AddRacersRequest;
import com.teddycrane.racemanagement.model.race.request.CreateRaceRequest;
import com.teddycrane.racemanagement.model.race.request.StartRaceRequest;
import com.teddycrane.racemanagement.model.race.request.UpdateRaceRequest;
import com.teddycrane.racemanagement.model.race.response.RaceCollectionResponse;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/race")
public interface RaceApi {

  @GetMapping(consumes = "application/json", produces = "application/json")
  @Operation(description = "Get all races")
  @ApiResponse(responseCode = "200", description = "Found all races")
  ResponseEntity<RaceCollectionResponse> getAllRaces();

  @GetMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
  @Operation(description = "Get single race")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully found race",
            content = {@Content(schema = @Schema(implementation = Race.class))}),
        @ApiResponse(
            responseCode = "404",
            description = "No race found",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id provided",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
      })
  ResponseEntity<? extends Response> getRace(@PathVariable("id") String id);

  @PostMapping(produces = "application/json", consumes = "application/json")
  @Operation(description = "Create new race")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully created Race",
            content = {@Content(schema = @Schema(implementation = Race.class))}),
        @ApiResponse(
            responseCode = "409",
            description = "Could not create race: A race with the same name already exists",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
      })
  ResponseEntity<? extends Response> createRace(@Valid @RequestBody CreateRaceRequest request);

  @PostMapping(
      value = "/{raceId}/add-racers",
      produces = "application/json",
      consumes = "application/json")
  @Operation(description = "Add racers to existing race")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully added Racers",
            content = {@Content(schema = @Schema(implementation = Race.class))}),
        @ApiResponse(
            responseCode = "404",
            description = "No race found for the id",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(
            responseCode = "409",
            description = "New edits are available, fetch data and retry",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
      })
  ResponseEntity<? extends Response> addRacersToRace(
      @PathVariable String raceId, @Valid @RequestBody AddRacersRequest request);

  @PatchMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
  @Operation(description = "Update race metadata")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully updated Race",
            content = {@Content(schema = @Schema(implementation = Race.class))}),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(
            responseCode = "404",
            description = "No race found",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(
            responseCode = "409",
            description = "Timestamp not up to date",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
      })
  ResponseEntity<? extends Response> updateRace(
      @PathVariable("id") String id, @Valid @RequestBody UpdateRaceRequest request);

  @GetMapping(
      value = "/races-for-racer/{racerId}",
      produces = "application/json",
      consumes = "application/json")
  @Operation(description = "Get races that the specified racer participated in")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully found data",
            content = {@Content(schema = @Schema(implementation = RaceCollectionResponse.class))}),
        @ApiResponse(
            responseCode = "404",
            description = "Racer not found",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
      })
  ResponseEntity<? extends Response> getRacesForRacer(@PathVariable("racerId") String racerId);

  @PutMapping(
      value = "/{id}/start-race",
      produces = "application/json",
      consumes = "application/json")
  @Operation(description = "Starts the specified race")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Race successfully started",
            content = {@Content(schema = @Schema(implementation = Race.class))}),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request - Invalid UUID",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(
            responseCode = "404",
            description = "Race not found",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
      })
  ResponseEntity<? extends Response> startRace(
      @PathVariable("id") String id, @Valid @RequestBody StartRaceRequest request);

  @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  @Operation(description = "Deletes the specified race")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted race"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
      })
  ResponseEntity<? extends Response> deleteRace(@PathVariable("id") String id);
}
