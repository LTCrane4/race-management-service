package com.teddycrane.racemanagement.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RaceTest {
  private Race race;
  private Racer racer;

  @BeforeEach
  void setUp() {
    race = TestResourceGenerator.generateRace(Category.CAT1);
    racer = TestResourceGenerator.generateRacer(Category.CAT1);
  }

  @Test
  @DisplayName("Race should successfully add racer to racer list")
  void addRacerShouldAddRacerSuccessfully() {
    var existingList = new ArrayList<>(race.getRacers());

    // action
    race.addRacer(racer);

    assertAll(
        () ->
            assertNotEquals(
                existingList,
                race.getRacers(),
                "The list of racers should have changed when the racer was added"),
        () ->
            assertTrue(
                race.getRacers().contains(racer),
                "The racer list should contain the newly added racer"));
  }

  @Test
  @DisplayName("Race should not add a racer if it already exists in the racer list")
  void addRacerShouldNotAdd() {
    race.setRacers(List.of(racer));
    var existingList = race.getRacers();

    race.addRacer(racer);

    assertEquals(existingList, race.getRacers(), "The lists should have not changed");
  }
}
