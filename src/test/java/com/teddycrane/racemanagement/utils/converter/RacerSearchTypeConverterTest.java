package com.teddycrane.racemanagement.utils.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.teddycrane.racemanagement.enums.RacerSearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RacerSearchTypeConverterTest {

  private RacerSearchTypeConverter converter;

  @BeforeEach
  void setUp() {
    this.converter = new RacerSearchTypeConverter();
  }

  @Test
  void racerTypeConverterShouldConvert() {
    assertEquals(
        RacerSearchType.CATEGORY,
        this.converter.convert("category"),
        "The converter should convert and return the converted value");
  }

  @Test
  void racerTypeConverterShouldReturnNull() {
    assertNull(
        this.converter.convert("bad value"),
        "The converter should return null when an invalid value is given");
  }
}
