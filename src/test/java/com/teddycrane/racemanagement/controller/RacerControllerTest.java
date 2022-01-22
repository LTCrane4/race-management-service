package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.BadRequestException;
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

class RacerControllerTest {

  @Mock private RacerService racerService;

  private RacerController racerController;

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

    RacerCollectionResponse actual = this.racerController.getAllRacers();

    assertAll(
        () -> assertNotNull(actual, "The response should not be null"),
        () ->
            assertEquals(
                new RacerCollectionResponse(expectedList).getRacers(),
                actual.getRacers(),
                "The lists should match the expected list"));
  }

  @Test
  void shouldGetRacer() {
    when(this.racerService.getRacer(testId)).thenReturn(expected);

    Racer actual = this.racerController.getRacer(testString);
    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () -> assertEquals(expected, actual, "The expected and actual results should be equal"));
  }

  @Test
  void getShouldHandleBadId() {
    assertThrows(
        BadRequestException.class,
        () -> this.racerController.getRacer("bad id"),
        "The controller should throw a bad request exception if an invalid id is provided");
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

    var actual = this.racerController.createRacer(request);

    assertAll(
        () -> assertNotNull(actual, "The result should not be null"),
        () -> assertEquals(expected, actual, "The result should match the expected value"));
  }
}
