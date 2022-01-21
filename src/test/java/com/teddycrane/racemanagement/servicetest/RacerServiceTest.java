package com.teddycrane.racemanagement.servicetest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import com.teddycrane.racemanagement.services.RacerService;
import com.teddycrane.racemanagement.services.RacerServiceImpl;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RacerServiceTest {

  @Mock private RacerRepository racerRepository;

  private RacerService racerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.racerService = new RacerServiceImpl(racerRepository);
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
}
