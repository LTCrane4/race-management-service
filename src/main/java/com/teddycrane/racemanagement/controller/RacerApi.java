package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.RacerSearchType;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.DeleteRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.UpdateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @DeleteMapping
  @Operation(summary = "Soft delete a racer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted racer"),
        @ApiResponse(responseCode = "404", description = "No racer found for id"),
        @ApiResponse(responseCode = "409", description = "Updated timestamps do not match")
      })
  ResponseEntity<Racer> deleteRacer(@Valid @RequestBody DeleteRacerRequest request);

  @GetMapping("/search")
  @Operation(summary = "Search Racers")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully found racers matching query params",
            content = {@Content(schema = @Schema(implementation = RacerCollectionResponse.class))}),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
      })
  ResponseEntity<? extends Response> searchRacers(
      @RequestParam("type") RacerSearchType searchType, @RequestParam("value") String searchValue);
}
