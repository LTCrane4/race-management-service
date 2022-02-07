package com.teddycrane.racemanagement.utils.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.teddycrane.racemanagement.enums.SearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchTypeConverterTest {

  private SearchTypeConverter converter;

  @BeforeEach
  void setUp() {
    this.converter = new SearchTypeConverter();
  }

  @Test
  void shouldConvert() {
    assertEquals(
        SearchType.TYPE,
        this.converter.convert("type"),
        "The converter should convert valid enum values");
  }

  @Test
  void shouldThrow() {
    assertNull(
        this.converter.convert("bad value"),
        "The converter should return null if the input is not a valid enum value");
  }
}
