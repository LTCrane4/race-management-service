package com.teddycrane.racemanagement.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.services.RaceService;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RaceControllerTest {

  @Mock private RaceService raceService;

  private RaceApi raceController;
  private Collection<Race> races = TestResourceGenerator.generateRaceList();

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
}
