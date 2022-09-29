package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.RacerSearchType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.racer.RacerDto;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.DeleteRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.SearchRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.UpdateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.MediaType;
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
            mediaType = MediaType.APPLICATION_JSON_VALUE,
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
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = RacerDto.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid racer id",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "No racer found",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            })
      })
  ResponseEntity<RacerDto> getRacer(@PathVariable("id") String id)
      throws BadRequestException, NotFoundException;

  @PostMapping
  @Operation(description = "Creates a new racer")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Successfully created racer",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = RacerDto.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "409",
            description = "Attempted duplicate created",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            })
      })
  ResponseEntity<RacerDto> createRacer(@NonNull @Valid @RequestBody CreateRacerRequest request)
      throws DuplicateItemException, BadRequestException;

  @PatchMapping("/{id}")
  @Operation(description = "Update racer data")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully updated racer",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = RacerDto.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid racer id",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "No racer found",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "409",
            description = "Timestamps do not match",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            })
      })
  ResponseEntity<RacerDto> updateRacer(
      @PathVariable("id") String id, @Valid @RequestBody @NonNull UpdateRacerRequest request)
      throws BadRequestException, NotFoundException, ConflictException;

  @DeleteMapping
  @Operation(summary = "Soft delete a racer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted racer"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request: Unable to delete racer",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "No racer found for id",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "409",
            description = "Updated timestamps do not match",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            })
      })
  ResponseEntity<Response> deleteRacer(@Valid @RequestBody DeleteRacerRequest request)
      throws BadRequestException, ConflictException, NotFoundException;

  // TODO remove this endpoint
  @GetMapping("/search")
  @Operation(summary = "Search Racers")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully found racers matching query params",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = RacerCollectionResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            }),
      })
  @Deprecated
  ResponseEntity<? extends Response> searchRacers(
      @RequestParam("type") RacerSearchType searchType, @RequestParam("value") String searchValue);

  @PostMapping("/search")
  @Operation(summary = "Search Racers (new)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found racers",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = RacerCollectionResponse.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponse.class))
            })
      })
  ResponseEntity<RacerCollectionResponse> searchRacersNew(
      @Valid @NonNull SearchRacerRequest request) throws BadRequestException;
}
