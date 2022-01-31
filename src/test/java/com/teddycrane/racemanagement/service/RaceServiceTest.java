package com.teddycrane.racemanagement.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.repositories.RaceRepository;
import com.teddycrane.racemanagement.services.RaceService;
import com.teddycrane.racemanagement.services.RaceServiceImpl;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

  private final Race expected = TestResourceGenerator.generateRace();
  private final UUID testId = UUID.randomUUID();
  @Mock private RaceRepository raceRepository;
  private RaceService raceService;
  private Collection<Race> expectedList;

  @BeforeEach
  void setUp() {
    this.raceService = new RaceServiceImpl(raceRepository);
    this.expectedList = TestResourceGenerator.generateRaceList();
  }

  @Test
  @DisplayName("Should get all races")
  void shouldGetAllRaces() {
    when(this.raceRepository.findAll()).thenReturn((List<Race>) expectedList);

    var result = this.raceService.getAllRaces();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                expectedList.size(),
                result.size(),
                "The result size and the expected size should match"));
  }

  @Test
  @DisplayName("Should return an empty list if no races are found")
  void shouldReturnEmptyIfNoRacesFound() {
    when(this.raceRepository.findAll()).thenReturn(List.of());

    var result = this.raceService.getAllRaces();

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(0, result.size(), "The result list should be empty"));
  }

  @Test
  @DisplayName("Get single race should return a race")
  void shouldReturnSingleRace() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.of(expected));

    var result = this.raceService.getRace(testId);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(expected, result, "The result should equal the expected value"));
  }

  @Test
  @DisplayName("Get single race should throw a NotFoundException if the file is not found")
  void shouldThrowNotFoundIfRaceIsNotFound() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> this.raceService.getRace(testId));
  }
}
