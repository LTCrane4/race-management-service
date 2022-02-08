package com.teddycrane.racemanagement.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Category {
  CAT1("cat1"),
  CAT2("cat2"),
  CAT3("cat3"),
  CAT4("cat4"),
  CAT5("cat5"),
  OTHER("other"),
  UNKNOWN("unknown");

  private final String text;

  Category(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return this.text;
  }

  public static boolean hasValue(String value) {
    return Arrays.asList(Category.values()).stream()
        .map(item -> item.toString())
        .collect(Collectors.toList())
        .contains(value);
  }
}
