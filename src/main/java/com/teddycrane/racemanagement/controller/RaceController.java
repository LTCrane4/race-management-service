package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.InternalServerError;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.race.RaceDto;
import com.teddycrane.racemanagement.model.race.request.AddRacersRequest;
import com.teddycrane.racemanagement.model.race.request.CreateRaceRequest;
import com.teddycrane.racemanagement.model.race.request.SearchRaceRequest;
import com.teddycrane.racemanagement.model.race.request.StartRaceRequest;
import com.teddycrane.racemanagement.model.race.request.UpdateRaceRequest;
import com.teddycrane.racemanagement.model.race.response.RaceCollectionResponse;
import com.teddycrane.racemanagement.model.response.GenericResponse;
import com.teddycrane.racemanagement.services.RaceService;
import com.teddycrane.racemanagement.utils.mapper.RaceMapper;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RaceController extends BaseController implements RaceApi {

  private final RaceService raceService;
  private final RaceMapper mapper;

  public RaceController(RaceService raceService, RaceMapper mapper) {
    super();
    this.raceService = raceService;
    this.mapper = mapper;
  }

  @Override
  public ResponseEntity<RaceCollectionResponse> getAllRaces() {
    logger.info("getAllRaces called");

    return ResponseEntity.ok(
        new RaceCollectionResponse(
            this.mapper.convertEntityListToDTOList(this.raceService.getAllRaces())));
  }

  @Override
  public ResponseEntity<RaceDto> getRace(String id) {
    logger.info("getRace called for id: {}", id);

    try {
      UUID raceId = UUID.fromString(id);
      return ResponseEntity.ok(this.mapper.convertEntityToDTO(this.raceService.getRace(raceId)));
    } catch (IllegalArgumentException e) {
      logger.error("Invalid UUID provided");
      throw new BadRequestException("Invalid UUID provided!");
    } catch (NotFoundException e) {
      logger.error("No race found for the id {}", id);
      throw new NotFoundException(String.format("No race found for the id %s", id));
    }
  }

  @Override
  public ResponseEntity<RaceDto> createRace(@NonNull CreateRaceRequest request) {
    logger.info("createRace called");
    String name = request.getName();
    Category category = request.getCategory();
    List<UUID> racers = List.of();

    // filter out null elements
    if (request.getRacerIds() != null) {
      racers = request.getRacerIds().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    try {
      return ResponseEntity.ok(
          this.mapper.convertEntityToDTO(this.raceService.createRace(name, category, racers)));
    } catch (ConflictException e) {
      logger.error("Name Collision: Cannot create duplicate race");
      throw new ConflictException("Cannot create a duplicate of an existing race!");
    }
  }

  @Override
  public ResponseEntity<RaceDto> addRacersToRace(String raceId, @NonNull AddRacersRequest request)
      throws BadRequestException, ConflictException, NotFoundException {
    logger.info("addRacersToRace called");

    try {
      UUID id = UUID.fromString(raceId);
      Instant updatedTimestamp = Instant.parse(request.getUpdatedTimestamp());

      return ResponseEntity.ok(
          this.mapper.convertEntityToDTO(
              this.raceService.addRacersToRace(id, request.getRacerIds(), updatedTimestamp)));
    } catch (IllegalArgumentException | DateTimeParseException e) {
      logger.error("A provided parameter was not provided in a valid format");
      throw new BadRequestException(
          "One of the required parameters was not provided in a valid format");
    } catch (NotFoundException e) {
      logger.error(
          "An error occurred when attempting to resolve data for the following UUID: {}",
          e.getMessage());
      throw new NotFoundException(String.format("No entries found for the id %s", e.getMessage()));
    } catch (ConflictException e) {
      logger.error("Newer data has been provided.");
      throw new ConflictException("Newer data exists.  Please re-fetch and try again.");
    }
  }

  @Override
  public ResponseEntity<RaceDto> updateRace(String id, UpdateRaceRequest request)
      throws BadRequestException, ConflictException, NotFoundException {
    logger.info("updateRace called");

    try {
      UUID raceId = UUID.fromString(id);

      // verify required parameters are present
      if (!request.isValidRequest())
        throw new BadRequestException("Not all required parameters are provided");

      return ResponseEntity.ok(
          this.mapper.convertEntityToDTO(
              this.raceService.updateRace(
                  raceId,
                  request.getName(),
                  request.getCategory(),
                  Instant.parse(request.getUpdatedTimestamp()))));
    } catch (IllegalArgumentException | DateTimeParseException e) {
      logger.error("One of the required metadata values is not valid");
      throw new BadRequestException(
          String.format(
              "One of the provided audit values is invalid: ID: %s, timestamp: %S",
              id, request.getUpdatedTimestamp()));
    } catch (BadRequestException e) {
      logger.error("Update parameters not provided");
      throw new BadRequestException("Not enough parameters provided!");
    } catch (ConflictException e) {
      logger.error("The updated timestamp is not the most recent");
      throw new ConflictException(
          "The updatedTimestamp is not the most recent.  Please re-fetch data and try again.");
    } catch (NotFoundException e) {
      logger.error("No Race found for the id {}", id);
      throw new NotFoundException(String.format("No race found for the id %s", id));
    }
  }

  @Override
  public ResponseEntity<RaceCollectionResponse> getRacesForRacer(String racerId)
      throws BadRequestException, NotFoundException {
    logger.info("getRacesForRacer called");

    try {
      UUID id = UUID.fromString(racerId);
      return ResponseEntity.ok(
          RaceCollectionResponse.builder()
              .data(this.mapper.convertEntityListToDTOList(this.raceService.getRacesForRacer(id)))
              .build());
    } catch (IllegalArgumentException e) {
      logger.error("The provided id is not a valid id!");
      throw new BadRequestException("The provided id was not a valid id!");
    } catch (NotFoundException e) {
      logger.error("No racer found for the id {}", racerId);
      throw new NotFoundException(String.format("No Racer found for the id %s", racerId));
    }
  }

  @Override
  public ResponseEntity<RaceDto> startRace(String id, @Valid StartRaceRequest request)
      throws BadRequestException, ConflictException, NotFoundException {
    logger.info("startRace called");

    try {
      UUID raceId = UUID.fromString(id);
      return ResponseEntity.ok(
          this.mapper.convertEntityToDTO(
              this.raceService.startRace(raceId, Instant.parse(request.getUpdatedTimestamp()))));
    } catch (IllegalArgumentException e) {
      logger.error("Invalid UUID provided");
      throw new BadRequestException("The provided id was not in a valid format");
    } catch (NotFoundException e) {
      logger.error("No race found for the id {}", id);
      throw new NotFoundException(String.format("No race found for the id %s", id));
    } catch (ConflictException e) {
      logger.error("Conflict detected");
      throw new ConflictException(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<GenericResponse> deleteRace(String id)
      throws BadRequestException, NotFoundException, InternalServerError {
    logger.info("deleteRace called");

    try {
      UUID raceId = UUID.fromString(id);
      if (this.raceService.deleteRace(raceId)) {
        return ResponseEntity.noContent().build();
      } else {
        throw new InternalServerError();
      }
    } catch (IllegalArgumentException e) {
      logger.error("Unable to parse the provided id");
      throw new BadRequestException(String.format("Unable to parse the id %s", id));
    } catch (NotFoundException e) {
      logger.error("No race found!");
      throw new NotFoundException(String.format("No race found for the id %s", id));
    }
  }

  @Override
  public ResponseEntity<RaceCollectionResponse> searchRaces(@Valid SearchRaceRequest request)
      throws BadRequestException {
    logger.info("searchRaces called");

    if (request.isValidRequest()) {
      return ResponseEntity.ok(
          new RaceCollectionResponse(
              this.mapper.convertEntityListToDTOList(this.raceService.searchRaces(request))));
    } else {
      logger.error("Invalid request!");
      throw new BadRequestException("The request must not be empty");
    }
  }
}
