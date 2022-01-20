package com.teddycrane.racemanagement.config.converter;

import com.teddycrane.racemanagement.enums.SearchType;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchTypeConverter extends StringToEnumConverter<SearchType> {

  @Override
  public SearchType convert(String source) {
    try {
      return SearchType.valueOf(source.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
