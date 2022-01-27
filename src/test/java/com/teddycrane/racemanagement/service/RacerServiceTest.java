package com.teddycrane.racemanagement.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import com.teddycrane.racemanagement.services.RacerService;
import com.teddycrane.racemanagement.services.RacerServiceImpl;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("Should successfully create a racer")
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

  @Test
  @DisplayName("Should update a racer")
  void shouldUpdateRacer() {
    when(this.racerRepository.findById(testId)).thenReturn(Optional.of(expected));
    when(this.racerRepository.save(any(Racer.class)))
        .thenAnswer(arguments -> arguments.getArgument(0));
    var expectedRacer = new Racer(expected);
    expectedRacer.setFirstName("First");
    expectedRacer.setLastName("Last");
    expectedRacer.setMiddleName("Middle");
    expectedRacer.setTeamName("Team");
    expectedRacer.setPhoneNumber("123-465-789");
    expectedRacer.setEmail("newemail@email.fake");

    var result =
        this.racerService.updateRacer(
            testId,
            expected.getUpdatedTimestamp(),
            "First",
            "Last",
            "Middle",
            "Team",
            "123-456-789",
            "newemail@email.fake");

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals("First", result.getFirstName(), "The first name should have been updated"),
        () -> assertEquals("Last", result.getLastName(), "The last name should have been updated"),
        () ->
            assertEquals(
                "Middle", result.getMiddleName(), "The middle name should have been updated"),
        () -> assertEquals("Team", result.getTeamName(), "The team name should have been updated"),
        () ->
            assertEquals(
                "123-456-789",
                result.getPhoneNumber(),
                "The phone number should have been updated"),
        () ->
            assertEquals(
                "newemail@email.fake", result.getEmail(), "The email should have been updated"));
  }

  @Test
  @DisplayName("Should not update if the updated time stamps do not match")
  void shouldNotUpdateIfTimestampsDontMatch() {
    when(this.racerRepository.findById(testId)).thenReturn(Optional.of(expected));

    assertThrows(
        ConflictException.class,
        () ->
            this.racerService.updateRacer(
                testId, new Date(System.currentTimeMillis()), null, null, null, null, null, null));
  }

  @Test
  @DisplayName("Should throw an exception if the racer is not found")
  void shouldThrowWhenRacerNotFound() {
    when(this.racerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () ->
            this.racerService.updateRacer(testId, new Date(), null, null, null, null, null, null));
  }
}
