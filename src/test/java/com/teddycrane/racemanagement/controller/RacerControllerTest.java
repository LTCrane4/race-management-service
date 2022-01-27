package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.UpdateRacerRequest;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.services.RacerService;
import java.util.Date;
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
class RacerControllerTest {

  @Mock private RacerService racerService;

  private RacerApi racerController;

  private Racer expected;
  private UUID testId;
  private String testString;

  @BeforeEach
  void setUp() {
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
            .category(Category.CAT2.toString())
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
            .category(Category.CAT2.toString())
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

  @Test
  void shouldReturn400WhenBadCategoryValueIsProvided() {
    CreateRacerRequest request =
        CreateRacerRequest.builder()
            .firstName("Test")
            .lastName("lname")
            .category("invalid category")
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
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status code should be 400"));
  }

  @Test
  @DisplayName("Should update if all parameters are provided")
  void shouldUpdateRacer() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Date.class),
            eq("New"),
            eq("Last"),
            eq("Middle"),
            eq("New Team Name"),
            eq("123-456-789"),
            eq("newemail@email.fake")))
        .thenReturn(expected);

    var request =
        UpdateRacerRequest.builder()
            .firstName("New")
            .lastName("Last")
            .middleName("Middle")
            .teamName("New Team Name")
            .phoneNumber("123-456-789")
            .email("newemail@email.fake")
            .build();

    var result = this.racerController.updateRacer(testString, request);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(body, "The body should not be null"),
        () -> assertEquals(expected, body, "The body should match the expected value"));
  }

  @Test
  @DisplayName("Should update if the email is provided")
  void shouldUpdateIfOnlyEmailProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Date.class),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            eq("newemail@email.fake")))
        .thenReturn(expected);

    UpdateRacerRequest request = UpdateRacerRequest.builder().email("newemail@email.fake").build();

    var result = this.racerController.updateRacer(testString, request);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(body, "The body should not be null"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Should update if a phone number is provided")
  void shouldUpdateIfPhoneNumberProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Date.class),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            eq("123-456-789"),
            eq("newemail@email.fake")))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .phoneNumber("123-456-789")
            .build();

    var result = this.racerController.updateRacer(testString, request);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(body, "The body should not be null"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Should update when a team name is provided")
  void shouldUpdateWhenTeamNameIsProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Date.class),
            isNull(),
            isNull(),
            isNull(),
            eq("New Team Name"),
            eq("123-456-789"),
            eq("newemail@email.fake")))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .phoneNumber("123-456-789")
            .teamName("New Team Name")
            .build();

    var result = this.racerController.updateRacer(testString, request);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(body, "The body should not be null"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Should update when a middle name is provided")
  void shouldUpdateWhenMiddleNameIsProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Date.class),
            isNull(),
            isNull(),
            eq("Middle"),
            eq("New Team Name"),
            eq("123-456-789"),
            eq("newemail@email.fake")))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .phoneNumber("123-456-789")
            .teamName("New Team Name")
            .middleName("Middle")
            .build();

    var result = this.racerController.updateRacer(testString, request);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(body, "The body should not be null"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Should update when a last name is provided")
  void shouldUpdateWhenLastNameIsProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Date.class),
            isNull(),
            isNull(),
            eq("Middle"),
            eq("New Team Name"),
            eq("123-456-789"),
            eq("newemail@email.fake")))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .phoneNumber("123-456-789")
            .teamName("New Team Name")
            .middleName("Middle")
            .build();

    var result = this.racerController.updateRacer(testString, request);
    var body = result.getBody();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(body, "The body should not be null"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Should error if the request body is empty")
  void shouldErrorIfNoParamsProvided() {
    var result = this.racerController.updateRacer(testString, new UpdateRacerRequest());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result.getStatusCode(), "The status should be 400"));
  }

  @Test
  @DisplayName("Handles service errors")
  void shouldHandleServiceErrors() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Date.class),
            anyString(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull()))
        .thenThrow(NotFoundException.class);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .updatedTimestamp(System.currentTimeMillis())
            .firstName("test")
            .build();
    var result1 = this.racerController.updateRacer(testString, request);

    assertAll(
        () -> assertNotNull(result1, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.NOT_FOUND, result1.getStatusCode(), "The status code should be 404"));

    var result2 = this.racerController.updateRacer("not valid", request);

    assertAll(
        () -> assertNotNull(result2, "The request should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST, result2.getStatusCode(), "The status should be 400"));
  }

  @Test
  @DisplayName(
      "Handles when a racer has been updated more recently than the submitted updated timestamp")
  void shouldReturnConflictWhenTimestampsDoNotMatch() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Date.class),
            anyString(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull()))
        .thenThrow(ConflictException.class);

    var request =
        UpdateRacerRequest.builder()
            .updatedTimestamp(System.currentTimeMillis())
            .firstName("test")
            .build();

    var result = this.racerController.updateRacer(testString, request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(HttpStatus.CONFLICT, result.getStatusCode(), "The status should be 409"));
  }
}
