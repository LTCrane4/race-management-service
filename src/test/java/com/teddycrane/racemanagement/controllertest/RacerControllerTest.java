package com.teddycrane.racemanagement.controllertest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.controller.RacerController;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import com.teddycrane.racemanagement.services.RacerService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RacerControllerTest {
  @Mock private RacerService racerService;

  private RacerController racerController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.racerController = new RacerController(this.racerService);
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
}
