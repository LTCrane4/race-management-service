package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.CreateRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.DeleteRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.SearchRacerRequest;
import com.teddycrane.racemanagement.model.racer.request.UpdateRacerRequest;
import com.teddycrane.racemanagement.services.RacerService;
import com.teddycrane.racemanagement.utils.mapper.RacerMapper;
import java.time.Instant;
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
        () -> assertNotNull(body, "The response body should not be null"));
  }

  @Test
  void shouldGetRacer() {
    when(this.racerService.getRacer(testId)).thenReturn(expected);

    var result = this.racerController.getRacer(testString);
    var body = RacerMapper.convertDTOToEntity(result.getBody());
    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status should be 200"),
        () -> assertEquals(expected, body, "The expected and actual results should be equal"));
  }

  @Test
  void getShouldHandleBadId() {

    assertThrows(
        BadRequestException.class,
        () -> this.racerController.getRacer("not a valid id"),
        "A BadRequestException should be thrown");
  }

  @Test
  @DisplayName("Get Racer should throw a 404 when a racer is not found")
  void shouldHandleNotFound() {
    when(this.racerService.getRacer(any(UUID.class))).thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class,
        () -> this.racerController.getRacer(UUID.randomUUID().toString()),
        "A NotFoundException should be thrown");
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
    var body = RacerMapper.convertDTOToEntity(result.getBody());

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

    assertThrows(
        DuplicateItemException.class,
        () -> this.racerController.createRacer(request),
        "A DuplicateItemException should be thrown");
  }

  @Test
  @DisplayName("Should throw a BadRequestException if a category is invalid")
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
    assertThrows(
        BadRequestException.class,
        () -> this.racerController.createRacer(request),
        "A BadRequestException should be thrown");
  }

  @Test
  @DisplayName("Should update if all parameters are provided")
  void shouldUpdateRacer() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Instant.class),
            eq("New"),
            eq("Last"),
            eq("Middle"),
            eq("New Team Name"),
            eq("123-456-789"),
            eq("newemail@email.fake"),
            any()))
        .thenReturn(expected);

    var request =
        UpdateRacerRequest.builder()
            .firstName("New")
            .lastName("Last")
            .middleName("Middle")
            .teamName("New Team Name")
            .phoneNumber("123-456-789")
            .email("newemail@email.fake")
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.racerController.updateRacer(testString, request);

    assertNotNull(result.getBody(), "The response body should not be null");
    var body = RacerMapper.convertDTOToEntity(result.getBody());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertEquals(expected, body, "The body should match the expected value"));
  }

  @Test
  @DisplayName("Should update if the email is provided")
  void shouldUpdateIfOnlyEmailProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Instant.class),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            eq("newemail@email.fake"),
            any()))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.racerController.updateRacer(testString, request);

    assertNotNull(result.getBody(), "The response body should not be null");
    var body = RacerMapper.convertDTOToEntity(result.getBody());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Should update if a phone number is provided")
  void shouldUpdateIfPhoneNumberProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Instant.class),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            eq("123-456-789"),
            eq("newemail@email.fake"),
            any()))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .phoneNumber("123-456-789")
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.racerController.updateRacer(testString, request);

    assertNotNull(result.getBody(), "The response body should not be null");
    var body = RacerMapper.convertDTOToEntity(result.getBody());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Should update when a team name is provided")
  void shouldUpdateWhenTeamNameIsProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Instant.class),
            isNull(),
            isNull(),
            isNull(),
            eq("New Team Name"),
            eq("123-456-789"),
            eq("newemail@email.fake"),
            any()))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .phoneNumber("123-456-789")
            .teamName("New Team Name")
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.racerController.updateRacer(testString, request);

    assertNotNull(result.getBody(), "The response body should not be null");
    var body = RacerMapper.convertDTOToEntity(result.getBody());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Should update when a middle name is provided")
  void shouldUpdateWhenMiddleNameIsProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Instant.class),
            isNull(),
            isNull(),
            eq("Middle"),
            eq("New Team Name"),
            eq("123-456-789"),
            eq("newemail@email.fake"),
            any()))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .phoneNumber("123-456-789")
            .teamName("New Team Name")
            .middleName("Middle")
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.racerController.updateRacer(testString, request);

    assertNotNull(result.getBody(), "The response body should not be null");
    var body = RacerMapper.convertDTOToEntity(result.getBody());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("update racer should update when a last name is provided")
  void shouldUpdateWhenLastNameIsProvided() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Instant.class),
            isNull(),
            isNull(),
            eq("Middle"),
            eq("New Team Name"),
            eq("123-456-789"),
            eq("newemail@email.fake"),
            any()))
        .thenReturn(expected);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .email("newemail@email.fake")
            .phoneNumber("123-456-789")
            .teamName("New Team Name")
            .middleName("Middle")
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.racerController.updateRacer(testString, request);

    assertNotNull(result.getBody(), "The response body should not be null");
    var body = RacerMapper.convertDTOToEntity(result.getBody());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertEquals(expected, body, "The body should be the expected value"));
  }

  @Test
  @DisplayName("Update racer should error if the request body is empty")
  void shouldErrorIfNoParamsProvided() {
    assertThrows(
        BadRequestException.class,
        () -> this.racerController.updateRacer(testString, new UpdateRacerRequest()),
        "A BadRequestException should be thrown");
  }

  @Test
  @DisplayName("Update racer should throw a NotFoundException when no racer is found")
  void shouldHandleServiceErrors() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Instant.class),
            anyString(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            any()))
        .thenThrow(NotFoundException.class);

    UpdateRacerRequest request =
        UpdateRacerRequest.builder()
            .updatedTimestamp(Instant.now().toString())
            .firstName("test")
            .build();

    assertThrows(
        NotFoundException.class,
        () -> this.racerController.updateRacer(testString, request),
        "A NotFoundException should be thrown");
  }

  @Test
  @DisplayName(
      "Handles when a racer has been updated more recently than the submitted updated timestamp")
  void shouldReturnConflictWhenTimestampsDoNotMatch() {
    when(this.racerService.updateRacer(
            eq(testId),
            any(Instant.class),
            anyString(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            any()))
        .thenThrow(ConflictException.class);

    assertThrows(
        ConflictException.class,
        () ->
            this.racerController.updateRacer(
                testString,
                UpdateRacerRequest.builder()
                    .updatedTimestamp(Instant.now().toString())
                    .firstName("test")
                    .build()),
        "A ConflictException should be thrown");
  }

  @Test
  @DisplayName("Update Racer should handle illegal timestamp formats")
  void updateRacerShouldHandleBadTimestamp() {
    assertThrows(
        BadRequestException.class,
        () ->
            this.racerController.updateRacer(
                testString, UpdateRacerRequest.builder().updatedTimestamp("invalid vlaue").build()),
        "A BadRequestException should be thrown");
  }

  @Test
  @DisplayName("Delete racer should successfully delete a racer")
  void deleteShouldBeSuccessful() {
    when(this.racerService.deleteRacer(eq(testId), any(Instant.class))).thenReturn(true);
    DeleteRacerRequest request =
        DeleteRacerRequest.builder()
            .id(testString)
            .updatedTimestamp(Instant.now().toString())
            .build();

    var result = this.racerController.deleteRacer(request);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                HttpStatus.NO_CONTENT, result.getStatusCode(), "The status should be 204"));
  }

  @Test
  @DisplayName("Delete racer should return not found if no racer is found")
  void deleteShouldReturn404IfNotFound() {
    when(this.racerService.deleteRacer(eq(testId), any(Instant.class)))
        .thenThrow(NotFoundException.class);

    assertThrows(
        NotFoundException.class,
        () ->
            this.racerController.deleteRacer(
                DeleteRacerRequest.builder()
                    .id(testId.toString())
                    .updatedTimestamp(Instant.now().toString())
                    .build()),
        "A NotFoundException should be thrown");
  }

  @Test
  @DisplayName("Delete Racer should return 409 if the timestamp is not valid")
  void deleteShouldReturn409() {
    when(this.racerService.deleteRacer(eq(testId), any(Instant.class)))
        .thenThrow(ConflictException.class);

    assertThrows(
        ConflictException.class,
        () ->
            this.racerController.deleteRacer(
                DeleteRacerRequest.builder()
                    .id(testString)
                    .updatedTimestamp(Instant.now().toString())
                    .build()),
        "A ConflictException should be thrown");
  }

  @Test
  @DisplayName(
      "Delete Racer should return a 500 if the service cannot delete a racer for any other reason")
  void deleteShouldReturn500() {
    when(this.racerService.deleteRacer(eq(testId), any(Instant.class))).thenReturn(false);

    var response =
        this.racerController.deleteRacer(
            DeleteRacerRequest.builder()
                .id(testString)
                .updatedTimestamp(Instant.now().toString())
                .build());

    assertAll(
        () -> assertNotNull(response, "The response should not be null"),
        () ->
            assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode(),
                "The response status should be 400"));
  }

  @Test
  @DisplayName("Delete Racer should handle invalid UUIDs")
  void deleteShouldHandleInvalidUUID() {
    assertThrows(
        BadRequestException.class,
        () ->
            this.racerController.deleteRacer(
                DeleteRacerRequest.builder()
                    .id("not valid")
                    .updatedTimestamp(Instant.now().toString())
                    .build()),
        "A BadRequestException should be thrown");
  }

  @Test
  @DisplayName("Search Racers (old) should return a 304")
  void searchRacersShouldReturn304() {
    var result = this.racerController.searchRacers(null, null);

    assertAll(
        () ->
            assertEquals(
                HttpStatus.MOVED_PERMANENTLY,
                result.getStatusCode(),
                "The status code should be 304"),
        () -> assertNotNull(result.getBody(), "The response body should contain a message"));
  }

  @Test
  @DisplayName("Search Racers (new) should return a list of racers")
  void searchRacersNewShouldReturnList() {
    var expectedList = TestResourceGenerator.generateRacerList(4);
    when(this.racerService.searchRacers(any())).thenReturn(expectedList);

    var request = SearchRacerRequest.builder().firstName("test").build();

    var result = this.racerController.searchRacersNew(request);

    assertAll(
        () -> assertNotNull(result, "The response should not be null"),
        () -> assertEquals(HttpStatus.OK, result.getStatusCode(), "The status code should be 200"),
        () -> assertNotNull(result.getBody(), "The response body should not be null"));
  }

  @Test
  @DisplayName("Search Racers (new) should handle invalid requests")
  void searchRacersNewShouldHandleBadRequests() {
    assertThrows(
        BadRequestException.class,
        () -> this.racerController.searchRacersNew(SearchRacerRequest.builder().build()),
        "Search Racers (new) should throw a BadRequestException");
  }
}
