package com.teddycrane.racemanagement.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import com.teddycrane.racemanagement.services.RacerService;
import com.teddycrane.racemanagement.services.RacerServiceImpl;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RacerServiceTest {

  @Mock private RacerRepository racerRepository;

  private RacerService racerService;

  private Racer expected;
  private UUID testId;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.racerService = new RacerServiceImpl(racerRepository);
    this.expected = TestResourceGenerator.generateRacer();
    this.testId = UUID.randomUUID();
  }

  @Test
  void shouldReturnRacers() {
    Collection<Racer> expected = TestResourceGenerator.generateRacerList(5);
    when(this.racerRepository.findAll()).thenReturn((List<Racer>) expected);

    Collection<Racer> actual = this.racerService.getAllRacers();
    assertAll(
        () -> assertNotNull(actual, "The response should not be null"),
        () -> assertEquals(expected, actual, "The list should match the expected value"));
  }

  @Test
  void shouldReturnSingleRacer() {
    when(this.racerRepository.findById(testId)).thenReturn(Optional.of(expected));

    var result = this.racerService.getRacer(testId);
    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(expected, result, "The result should equal the expected value"));
  }

  @Test
  void shouldThrowNotFoundErrorIfNoRacerIsFound() {
    when(this.racerRepository.findById(testId)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> this.racerService.getRacer(testId));
  }

  @Test
  void shouldCreateRacer() {
    when(this.racerRepository.save(any(Racer.class)))
        .thenAnswer((i) -> i.getArgument(0, Racer.class));

    var actual =
        this.racerService.createRacer(
            "first", "last", Category.CAT1, "middle", "team", "phone", "email@email.fake", 1);

    assertAll(
        () -> assertNotNull(actual, "the actual result should not be null"),
        () -> assertEquals("first", actual.getFirstName()));
  }
}
