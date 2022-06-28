package com.teddycrane.racemanagement.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    Racer defaultRacer = new Racer();
    assertNotNull(defaultRacer, "The default constructor should construct a valid racer");

    defaultRacer = Racer.copyOf(this.racer);
    Racer finalRacer = defaultRacer;

    Racer fullConstructor =
        new Racer("first", "Last", Category.CAT1, "middle", "team", "123456789", "email", 1);
    assertAll(
        () -> assertNotNull(finalRacer, "The copy constructor should construct valid racers"),
        () -> assertEquals(this.racer, finalRacer),
        () ->
            assertEquals(
                Category.CAT1.toString(),
                finalRacer.getCategory().toString(),
                "The racer category should match"),
        () -> assertNotNull(fullConstructor, "The full public constructor should not be null"));
  }

  @Test
  void shouldEqual() {
    Racer defaultRacer = new Racer();
    Racer other = Racer.copyOf(defaultRacer);
    assertEquals(defaultRacer, other, "The copyOf method should copy the racer");

    assertNotEquals(defaultRacer, "test");
  }
}
