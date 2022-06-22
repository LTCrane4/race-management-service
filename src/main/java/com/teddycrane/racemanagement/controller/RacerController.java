package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.enums.RacerSearchType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.racer.RacerDTO;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.DeleteRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.SearchRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.UpdateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.services.RacerService;
import com.teddycrane.racemanagement.utils.mapper.RacerMapper;
import com.teddycrane.racemanagement.utils.resolver.CategoryResolver;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RacerController extends BaseController implements RacerApi {

  private final RacerService racerService;

  public RacerController(RacerService racerService) {
    super();
    this.racerService = racerService;
  }

  @Override
  public ResponseEntity<RacerCollectionResponse> getAllRacers() {
    logger.info("getAllRacers called");
    return ResponseEntity.ok(
        new RacerCollectionResponse(
            RacerMapper.convertEntityListToDTO(this.racerService.getAllRacers())));
  }

  @Override
  public ResponseEntity<RacerDTO> getRacer(String id)
      throws BadRequestException, NotFoundException {
    logger.info("getRacer called");

    try {
      UUID userId = UUID.fromString(id);
      return ResponseEntity.ok(RacerMapper.convertEntityToDTO(this.racerService.getRacer(userId)));
    } catch (IllegalArgumentException e) {
      logger.error("Could not parse a valid UUID from the provided id");
      throw new BadRequestException("A valid ID was not provided");
    }
  }

  @Override
  public ResponseEntity<RacerDTO> createRacer(@NonNull CreateRacerRequest request)
      throws DuplicateItemException, BadRequestException {
    logger.info("createRacer called");

    try {
      Category c = CategoryResolver.resolveCategory(request.getCategory());

      return ResponseEntity.ok(
          RacerMapper.convertEntityToDTO(
              this.racerService.createRacer(
                  request.getFirstName(),
                  request.getLastName(),
                  c,
                  request.getMiddleName(),
                  request.getTeamName(),
                  request.getPhoneNumber(),
                  request.getEmail(),
                  request.getBibNumber())));
    } catch (IllegalArgumentException e) {
      logger.error("The provided value {} is not a valid category value", request.getCategory());
      throw new BadRequestException(
          String.format("The category value %s is not a valid Category", request.getCategory()));
    }
  }

  @Override
  public ResponseEntity<RacerDTO> updateRacer(String id, @NonNull @Valid UpdateRacerRequest request)
      throws BadRequestException, NotFoundException, ConflictException {
    logger.info("updateRacer called");

    // validate parameters here
    if (request.allParamsNull()) {
      logger.error("Cannot update a racer with no values");
      throw new BadRequestException("Cannot provide an empty request body");
    }

    try {
      UUID userId = UUID.fromString(id);

      Instant instant = Instant.parse(request.getUpdatedTimestamp());

      return ResponseEntity.ok(
          RacerMapper.convertEntityToDTO(
              this.racerService.updateRacer(
                  userId,
                  instant,
                  request.getFirstName(),
                  request.getLastName(),
                  request.getMiddleName(),
                  request.getTeamName(),
                  request.getPhoneNumber(),
                  request.getEmail())));
    } catch (IllegalArgumentException | DateTimeParseException e) {
      logger.error(
          "Unable to parse the id: {}, or timestamp: {}", id, request.getUpdatedTimestamp());
      throw new BadRequestException("Unable to parse the id or timestamp");
    }
  }

  @Override
  public ResponseEntity<Response> deleteRacer(@NonNull @Valid DeleteRacerRequest request)
      throws BadRequestException, ConflictException, NotFoundException {
    logger.info("deleteRacer called");

    try {
      UUID id = UUID.fromString(request.getId());
      Instant updatedTimestamp = Instant.parse(request.getUpdatedTimestamp());

      return this.racerService.deleteRacer(id, updatedTimestamp)
          ? ResponseEntity.noContent().build()
          : ResponseEntity.badRequest().build();
    } catch (IllegalArgumentException | DateTimeParseException e) {
      logger.error(
          "Unable to parse the id: {}, or timestamp: {}",
          request.getId(),
          request.getUpdatedTimestamp());
      throw new BadRequestException("Unable to parse the provided id or timestamp");
    }
  }

  @Override
  public ResponseEntity<? extends Response> searchRacers(
      RacerSearchType searchType, String searchValue) {
    logger.info("searchRacers called");

    switch (searchType) {
      case CATEGORY:
        {
          if (Category.hasValue(searchValue)) {
            return new ResponseEntity<>(
                new RacerCollectionResponse(
                    RacerMapper.convertEntityListToDTO(
                        this.racerService.searchRacers(searchType, searchValue))),
                HttpStatus.OK);
          }
          return new ResponseEntity<>(
              new ErrorResponse(
                  String.format("The category value %s is not a valid category!", searchValue)),
              HttpStatus.BAD_REQUEST);
        }
      case FIRST_NAME:
      case LAST_NAME:
      default:
        {
          return new ResponseEntity<>(
              new RacerCollectionResponse(
                  RacerMapper.convertEntityListToDTO(
                      this.racerService.searchRacers(searchType, searchValue))),
              HttpStatus.OK);
        }
    }
  }

  @Override
  public ResponseEntity<RacerCollectionResponse> searchRacersNew(
      @NonNull SearchRacerRequest request) throws BadRequestException {
    logger.info("searchRacers (new) called");

    if (!request.isValidRequest()) {
      logger.warn("Request is not valid");
      throw new BadRequestException("Request must have at least one non-null field");
    }
    return ResponseEntity.ok(
        RacerMapper.createRacerCollection(this.racerService.searchRacers(request)));
  }
}
