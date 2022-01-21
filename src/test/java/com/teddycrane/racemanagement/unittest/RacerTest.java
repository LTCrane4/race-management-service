package com.teddycrane.racemanagement.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.racer.Racer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RacerTest {

  private Racer racer;

  @BeforeEach
  void setUp() {
    this.racer = TestResourceGenerator.generateRacer();
  }

  @Test
  void shouldConstruct() {
    Racer racer = new Racer();
    assertNotNull(racer, "The default constructor should construct a valid racer");

    racer = new Racer(this.racer);
    Racer finalRacer = racer;
    assertAll(
        () -> assertNotNull(finalRacer, "The copy constructor should construct valid racers"),
        () -> assertEquals(this.racer, finalRacer),
        () ->
            assertEquals(
                Category.CAT1.toString(),
                finalRacer.getCategory().toString(),
                "The racer category should match"));
  }
}
