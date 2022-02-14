package com.teddycrane.racemanagement.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RaceRepository;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import com.teddycrane.racemanagement.services.RaceService;
import com.teddycrane.racemanagement.services.RaceServiceImpl;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
  @Mock private RacerRepository racerRepository;
  private RaceService raceService;
  private Collection<Race> expectedList;

  @BeforeEach
  void setUp() {
    this.raceService = new RaceServiceImpl(raceRepository, racerRepository);
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

  @Test
  @DisplayName("Create Race should successfully create a race")
  void shouldCreateRace() {
    when(this.raceRepository.findByName(anyString())).thenReturn(Optional.empty());
    when(this.raceRepository.save(any(Race.class))).thenReturn(expected);

    var result = this.raceService.createRace("name", Category.CAT2, List.of());

    assertNotNull(result, "The result should not be null");
  }

  @Test
  @DisplayName(
      "Create race should create a race if there is a race with the same name but a different"
          + " category")
  void createRaceShouldCreateWithDuplicateName() {
    expected.setCategory(Category.CAT1);
    when(this.raceRepository.findByName(anyString())).thenReturn(Optional.of(expected));
    when(this.raceRepository.save(any(Race.class)))
        .thenAnswer((arguments) -> arguments.getArgument(0));

    var result = this.raceService.createRace("Test", Category.CAT2, List.of());

    assertNotNull(result, "The result should not be null");
  }

  @Test
  @DisplayName("Create race should throw a conflict exception")
  void createRaceShouldThrowConflictException() {
    when(this.raceRepository.findByName(anyString())).thenReturn(Optional.of(expected));

    assertThrows(
        ConflictException.class,
        () -> this.raceService.createRace(expected.getName(), expected.getCategory(), List.of()));
  }

  @Test
  @DisplayName("Add Racers to Race should add racers successfully")
  void addRacersShouldAddRacers() {
    var list = List.of(TestResourceGenerator.generateRacer());
    when(this.raceRepository.findById(testId)).thenReturn(Optional.of(expected));
    when(this.racerRepository.findAllById(anyIterable())).thenReturn(list);
    when(this.raceRepository.save(any(Race.class)))
        .thenAnswer(arguments -> arguments.getArgument(0));

    var result =
        this.raceService.addRacersToRace(
            testId,
            list.stream().map(Racer::getId).collect(Collectors.toList()),
            expected.getUpdatedTimestamp());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertNotNull(result.getRacers(), "The list of racers should not be null"));
  }

  @Test
  @DisplayName("Add racers should throw a conflict exception")
  void addRacersShouldThrowConflictException() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.of(expected));

    assertThrows(
        ConflictException.class,
        () -> this.raceService.addRacersToRace(testId, List.of(), Instant.now()),
        "A ConflictException should be thrown");
  }

  @Test
  @DisplayName("Add racers should throw a NotFoundException when the Race is not found")
  void addRacersShouldThrowNotFound() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> this.raceService.addRacersToRace(testId, List.of(), Instant.now()),
        "A NotFoundException should be thrown if the race cannot be found");
  }

  @Test
  @DisplayName("Update Race should update a race")
  void updateRaceShouldUpdate() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.of(expected));
    when(this.raceRepository.save(any(Race.class)))
        .thenAnswer(arguments -> arguments.getArgument(0));

    var oldTimestamp = expected.getUpdatedTimestamp();
    var result = this.raceService.updateRace(testId, "New Name", Category.CAT2, oldTimestamp);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals("New Name", result.getName(), "The name should match the updated name"),
        () ->
            assertEquals(
                Category.CAT2,
                result.getCategory(),
                "The category should match the updated category"),
        () ->
            assertNotEquals(
                oldTimestamp,
                result.getUpdatedTimestamp(),
                "The audit timestamp should be changed with the update"));
  }

  @Test
  @DisplayName("Update race should do nothing if all update inputs are null")
  void updateRaceShouldDoNothingIfInputsAreNotProvided() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.of(expected));
    when(this.raceRepository.save(any(Race.class)))
        .thenAnswer(arguments -> arguments.getArgument(0));

    var oldTimestamp = expected.getUpdatedTimestamp();
    var result = this.raceService.updateRace(testId, null, null, expected.getUpdatedTimestamp());

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () ->
            assertEquals(
                oldTimestamp, result.getUpdatedTimestamp(), "The timestamp should not be updated"));
  }

  @Test
  @DisplayName("Update race should throw a NotFoundException if the race is not found")
  void updateRaceShouldThrowNotFound() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> this.raceService.updateRace(testId, null, null, null),
        "A NotFoundException should be thrown if no Race is found");
  }

  @Test
  @DisplayName("Update race should throw a ConflictException if the timestamps do not match")
  void updateRaceShouldThrowConflictException() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.of(expected));

    assertThrows(
        ConflictException.class,
        () -> this.raceService.updateRace(testId, null, null, Instant.now()),
        "A ConflictException should be thrown if the timestamps do not match");
  }

  private List<Race> setUpGetRacesForRacerList(Racer testRacer) {
    List<Race> list = (List<Race>) TestResourceGenerator.generateRaceList();
    list.get(0).addRacer(testRacer);
    list.get(list.size() - 1).addRacer(testRacer);

    return list;
  }

  @Test
  @DisplayName("Get Races for racer should return a list of races")
  void getRacesForRacerShouldReturnList() {
    var racer = TestResourceGenerator.generateRacer();
    var racerId = racer.getId();
    List<Race> raceList = setUpGetRacesForRacerList(racer);
    when(this.raceRepository.findAll()).thenReturn(raceList);

    var result = this.raceService.getRacesForRacer(racerId);

    assertAll(
        () -> assertNotNull(result, "The result should not be null"),
        () -> assertEquals(2, result.size(), "The list should have two entries"),
        () ->
            assertTrue(
                result.get(0).getRacers().contains(racer), "The list should contain the racer"));
  }

  @Test
  @DisplayName("Get Races for Racer should throw a NotFoundException")
  void getRacesForRacerShouldThrowNotFound() {
    when(this.racerRepository.existsById(any())).thenThrow(IllegalArgumentException.class);

    assertThrows(
        NotFoundException.class,
        () -> this.raceService.getRacesForRacer(UUID.randomUUID()),
        "The race service should throw an exception if the racer is not found");
  }

  @Test
  @DisplayName("Start race should start race at the current time")
  void startRaceShouldStartRace() {
    when(this.raceRepository.findById(testId)).thenReturn(Optional.of(expected));
    when(this.raceRepository.save(any())).thenAnswer(arguments -> arguments.getArgument(0));

    var result = this.raceService.startRace(testId, expected.getUpdatedTimestamp());

    assertAll(() -> assertNotNull(result, "The result should not be null"));
  }
}
