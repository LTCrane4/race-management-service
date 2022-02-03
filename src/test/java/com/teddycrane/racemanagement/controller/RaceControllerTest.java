package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.race.request.AddRacersRequest;
import com.teddycrane.racemanagement.model.race.request.CreateRaceRequest;
import com.teddycrane.racemanagement.model.race.request.UpdateRaceRequest;
import com.teddycrane.racemanagement.services.RaceService;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class RaceControllerTest {

  private final Race expected = TestResourceGenerator.generateRace();
  private final Collection<Race> races = TestResourceGenerator.generateRaceList();
  private final UUID testId = UUID.randomUUID();
  private final String testString = testId.toString();
  @Mock private RaceService raceService;
  private RaceApi raceController;

  @BeforeEach
  void setUp() {
    this.raceController = new RaceController(this.raceService);
  }

  @Test
  @DisplayName("Should get all races")
  void shouldGetAllRaces() {
    when(this.raceService.getAllRaces()).thenReturn((List<Race>) races);

    var result = this.raceController.getAllRaces();

    assertAll(() -> assertNotNull(result, "The result should not be null"));
  }

  @Test
  @DisplayName("Should get a single race")
  void shouldGetSingleRace() {
    when(this.raceService.getRace(testId)).thenReturn(expected);

    var result = this.raceController.getRace(testString);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(body, "The body should not be null"),
        () -> assertEquals(expected, body, "The body should equal the expected value"));
  }

  @Test
  @DisplayName("Get single race should throw 404 if a race is not found")
  void getRaceShouldReturn404() {
    when(this.raceService.getRace(testId)).thenThrow(NotFoundException.class);

    var result = this.raceController.getRace(testString);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode(), "The status should be 404"));
  }

  @Test
  @DisplayName("Get single race should return 400 if the id provided is not valid")
  void getRaceShouldReturn400() {
    var result = this.raceController.getRace("not a valid UUID");

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status should be 400"));
  }

  @Test
  @DisplayName("Create race should successfully create a race")
  void createRaceShouldCreate() {
    when(this.raceService.createRace(anyString(), any(Category.class), anyList()))
        .thenReturn(expected);

    var request = CreateRaceRequest.builder().name("Test Race").category(Category.CAT2).build();
    var result = this.raceController.createRace(request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be 200"));
  }

  @Test
  @DisplayName("Create race should successfully create a race with a pre-populated list of racers")
  void createRaceShouldCreateWithRacers() {
    when(this.raceService.createRace(eq("Test Race"), eq(Category.CAT2), anyList()))
        .thenReturn(expected);

    var request =
        CreateRaceRequest.builder()
            .name("Test Race")
            .category(Category.CAT2)
            .racerIds(List.of(UUID.randomUUID()))
            .build();

    var result = this.raceController.createRace(request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be 200"));
  }

  @Test
  @DisplayName("Create race should throw a 409 if there is a name collision")
  void createRaceShouldReturn409() {
    when(this.raceService.createRace(anyString(), any(Category.class), anyList()))
        .thenThrow(ConflictException.class);
    var request = CreateRaceRequest.builder().name("test").category(Category.CAT2).build();

    var result = this.raceController.createRace(request);

    assertAll(() -> assertNotNull(result, "The result should not be null"));
  }

  @Test
  @DisplayName("Add Racers should add racers to race")
  void addRacersShouldAddRacersToRace() {
    when(this.raceService.addRacersToRace(eq(testId), anyList(), any(Instant.class)))
        .thenReturn(expected);

    var request =
        AddRacersRequest.builder()
            .racerIds(List.of())
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.raceController.addRacersToRace(testString, request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be 200"));
  }

  @Test
  @DisplayName("Add Racers should not add racers if the service cannot parse the race id")
  void addRacersShouldReturn400WhenIdIsInvalid() {
    var request =
        AddRacersRequest.builder()
            .racerIds(List.of())
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.raceController.addRacersToRace("bad id", request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status should be 400"));
  }

  @Test
  @DisplayName("Add Racers should return 404 when the race service throws a NotFoundException")
  void addRacersShouldThrow404() {
    when(this.raceService.addRacersToRace(eq(testId), anyList(), any(Instant.class)))
        .thenThrow(NotFoundException.class);

    var request =
        AddRacersRequest.builder()
            .racerIds(List.of())
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.raceController.addRacersToRace(testString, request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode(), "The status should be 404"));
  }

  @Test
  @DisplayName("Add Racers should handle out of date requests")
  void addRacersShouldHandleBadTimestamp() {
    when(this.raceService.addRacersToRace(eq(testId), anyList(), any(Instant.class)))
        .thenThrow(ConflictException.class);

    var request =
        AddRacersRequest.builder()
            .racerIds(List.of())
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.raceController.addRacersToRace(testString, request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.CONFLICT, result.getStatusCode(), "The status should be 409"));
  }

  @Test
  @DisplayName("Update race should update a race with a valid request")
  void updateRaceShouldUpdate() {
    when(this.raceService.updateRace(
            eq(testId), anyString(), any(Category.class), any(Instant.class)))
        .thenReturn(expected);

    var request =
        UpdateRaceRequest.builder()
            .name("New Name")
            .category(Category.CAT1)
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.raceController.updateRace(testString, request);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be 200"),
        () -> assertNotNull(body, "The body should not be null"),
        () -> assertEquals(expected, body, "The body should match the expected value"));
  }

  @Test
  @DisplayName("Update race should handle bad UUID")
  void updateRaceShouldHandleBadId() {
    var request =
        UpdateRaceRequest.builder().name("test").updatedTimestamp(Instant.now().toString()).build();
    var result = this.raceController.updateRace("not a uuid", request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status should be 400"));
  }

  @Test
  @DisplayName("Update race should handle no requested changes")
  void updateRaceShouldHandleNoRequestedChanges() {
    var request = UpdateRaceRequest.builder().updatedTimestamp(Instant.now().toString()).build();

    var result = this.raceController.updateRace(testString, request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status should be 400"));
  }

  @Test
  @DisplayName("Update race should handle not found")
  void updateRaceShouldHandleNotFound() {
    when(this.raceService.updateRace(
            eq(testId), anyString(), any(Category.class), any(Instant.class)))
        .thenThrow(NotFoundException.class);

    var request =
        UpdateRaceRequest.builder()
            .name("test")
            .category(Category.CAT2)
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.raceController.updateRace(testString, request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode(), "The status should be 404"));
  }

  @Test
  @DisplayName("Update Race should hande conflict exceptions")
  void updateRaceShouldHandleConflictEx() {
    when(this.raceService.updateRace(
            eq(testId), anyString(), any(Category.class), any(Instant.class)))
        .thenThrow(ConflictException.class);

    var request =
        UpdateRaceRequest.builder()
            .name("test")
            .category(Category.CAT1)
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.raceController.updateRace(testString, request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.CONFLICT, result.getStatusCode(), "The status should be 409"));
  }
}
