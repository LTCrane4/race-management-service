package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.services.RacerService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class RacerControllerTest {

  @Mock private RacerService racerService;

  private RacerApi racerController;

  private Racer expected;
  private UUID testId;
  private String testString;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.racerController = new RacerController(this.racerService);
    this.expected = TestResourceGenerator.generateRacer();
    this.testId = UUID.randomUUID();
    this.testString = testId.toString();
  }

  @Test
  void shouldGetAllRacers() {
    List<Racer> expectedList = (List<Racer>) TestResourceGenerator.generateRacerList(5);
    when(this.racerService.getAllRacers()).thenReturn(expectedList);

    var result = this.racerController.getAllRacers();
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The response should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be 200"),
        () -> assertNotNull(body, "The response body should not be null"),
        () ->
            assertEquals(
                new RacerCollectionResponse(expectedList).getRacers(),
                body.getRacers(),
                "The lists should match the expected list"));
  }

  @Test
  void shouldGetRacer() {
    when(this.racerService.getRacer(testId)).thenReturn(expected);

    var result = this.racerController.getRacer(testString);
    var body = result.getBody();
    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be 200"),
        () -> assertEquals(expected, body, "The expected and actual results should be equal"));
  }

  @Test
  void getShouldHandleBadId() {

    var result = this.racerController.getRacer("not a valid id");

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status code should be 400"));
  }

  @Test
  void shouldHandleNotFound() {
    when(this.racerService.getRacer(any(UUID.class))).thenThrow(NotFoundException.class);

    var result = this.racerController.getRacer(testString);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode(), "The status should be 404"));
  }

  @Test
  void shouldCreateRacerWithAllFields() {
    when(this.racerService.createRacer(
            anyString(),
            anyString(),
            any(Category.class),
            anyString(),
            anyString(),
            anyString(),
            anyString(),
            anyInt()))
        .thenReturn(expected);

    CreateRacerRequest request =
        CreateRacerRequest.builder()
            .firstName("Test")
            .lastName("lname")
            .category(Category.CAT2)
            .middleName("mid")
            .teamName("team")
            .email("email@email.fake")
            .phoneNumber("Phone Number")
            .bibNumber(2)
            .build();

    var result = this.racerController.createRacer(request);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertEquals(expected, body, "The result should match the expected value"));
  }

  @Test
  void shouldReturnConflictWhenExistingIsFound() {
    when(this.racerService.createRacer(
            anyString(),
            anyString(),
            any(Category.class),
            anyString(),
            anyString(),
            anyString(),
            anyString(),
            anyInt()))
        .thenThrow(DuplicateItemException.class);

    CreateRacerRequest request =
        CreateRacerRequest.builder()
            .firstName("Test")
            .lastName("lname")
            .category(Category.CAT2)
            .middleName("mid")
            .teamName("team")
            .email("email@email.fake")
            .phoneNumber("Phone Number")
            .bibNumber(2)
            .build();

    var result = this.racerController.createRacer(request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.CONFLICT, result.getStatusCode(), "The status should be 409"));
  }
}
