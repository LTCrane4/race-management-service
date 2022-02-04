package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.race.request.AddRacersRequest;
import com.teddycrane.racemanagement.model.race.request.CreateRaceRequest;
import com.teddycrane.racemanagement.model.race.request.UpdateRaceRequest;
import com.teddycrane.racemanagement.model.race.response.RaceCollectionResponse;
// import com.teddycrane.racemanagement.services.RaceService;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.services.RaceService;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RaceController extends BaseController implements RaceApi {

  private final RaceService raceService;

  public RaceController(RaceService raceService) {
    super();
    this.raceService = raceService;
  }

  @NonNull
  private ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status) {
    var body = ErrorResponse.builder().message(message).build();
    return new ResponseEntity<ErrorResponse>(body, status);
  }

  @Override
  public ResponseEntity<RaceCollectionResponse> getAllRaces() {
    logger.info("getAllRaces called");

    return ResponseEntity.ok(new RaceCollectionResponse(this.raceService.getAllRaces()));
  }

  @Override
  public ResponseEntity<Race> getRace(String id) {
    logger.info("getRace called");

    try {
      UUID raceId = UUID.fromString(id);
      return ResponseEntity.ok(this.raceService.getRace(raceId));
    } catch (IllegalArgumentException e) {
      logger.error("Invalid UUID provided");
      return ResponseEntity.badRequest().build();
    } catch (NotFoundException e) {
      logger.error("No race found for the id {}", id);
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  public ResponseEntity<? extends Response> createRace(@NonNull CreateRaceRequest request) {
    logger.info("createRace called");
    String name = request.getName();
    Category category = request.getCategory();
    List<UUID> racers = List.of();

    // filter out null elements
    if (request.getRacerIds() != null) {
      racers = request.getRacerIds().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    try {
      return ResponseEntity.ok(this.raceService.createRace(name, category, racers));
    } catch (ConflictException e) {
      logger.error("Name Collision: Cannot create duplicate race");
      return this.createErrorResponse(
          "Cannot create a duplicate of an existing race!", HttpStatus.CONFLICT);
    }
  }

  @Override
  public ResponseEntity<? extends Response> addRacersToRace(
      String raceId, @NonNull AddRacersRequest request) {
    logger.info("addRacersToRace called");

    try {
      UUID id = UUID.fromString(raceId);
      Instant updatedTimestamp = Instant.parse(request.getUpdatedTimestamp());

      return ResponseEntity.ok(
          this.raceService.addRacersToRace(id, request.getRacerIds(), updatedTimestamp));
    } catch (IllegalArgumentException | DateTimeParseException e) {
      logger.error("A provided parameter was not provided in a valid format");
      return this.createErrorResponse(
          "One of the required parameters was not provided in a valid format",
          HttpStatus.BAD_REQUEST);
    } catch (NotFoundException e) {
      logger.error(
          "An error ocurred when attempting to resolve data for the following UUID: {}",
          e.getMessage());
      return this.createErrorResponse(
          String.format("No entries found for the id %s", e.getMessage()), HttpStatus.NOT_FOUND);
    } catch (ConflictException e) {
      logger.error("Newer data has been provided.");
      return this.createErrorResponse(
          "Newer data exists.  Please re-fetch and try again.", HttpStatus.CONFLICT);
    }
  }

  @Override
  public ResponseEntity<? extends Response> updateRace(String id, UpdateRaceRequest request) {
    logger.info("updateRace called");

    try {
      UUID raceId = UUID.fromString(id);

      // verify required parameters are present
      if (request.getName() == null && request.getCategory() == null) {
        throw new BadRequestException("Bad request!");
      }

      return new ResponseEntity<>(
          this.raceService.updateRace(
              raceId,
              request.getName(),
              request.getCategory(),
              Instant.parse(request.getUpdatedTimestamp())),
          HttpStatus.OK);
    } catch (IllegalArgumentException | DateTimeParseException e) {
      logger.error("One of the required metadata values is not valid");
      return this.createErrorResponse(
          String.format(
              "One of the provided audit values is invalid: ID: %s, timestamp: %S",
              id, request.getUpdatedTimestamp()),
          HttpStatus.BAD_REQUEST);
    } catch (BadRequestException e) {
      logger.error("Update parameters not provided");
      return this.createErrorResponse("Not enough parameters provided!", HttpStatus.BAD_REQUEST);
    } catch (ConflictException e) {
      logger.error("The updated timestamp is not the most recent");
      return this.createErrorResponse(
          "The updatedTimestamp is not the most recent.  Please re-fetch data and try again.",
          HttpStatus.CONFLICT);
    } catch (NotFoundException e) {
      logger.error("No Race found for the id {}", id);
      return this.createErrorResponse(
          String.format("No race found for the id %s", id), HttpStatus.NOT_FOUND);
    }
  }
}
