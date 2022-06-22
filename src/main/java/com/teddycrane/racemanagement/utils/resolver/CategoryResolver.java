package com.teddycrane.racemanagement.utils.resolver;

import com.teddycrane.racemanagement.enums.Category;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryResolver {
  public static Category resolveCategory(String input) throws IllegalArgumentException {
    Set<Category> values = new HashSet<>(List.of(Category.values()));

    if (values.stream().anyMatch((value) -> input.equalsIgnoreCase(value.toString()))) {
      return Category.valueOf(input.toUpperCase());
    } else {
      throw new IllegalArgumentException("The provided input is not a valid Category");
    }
  }
}
