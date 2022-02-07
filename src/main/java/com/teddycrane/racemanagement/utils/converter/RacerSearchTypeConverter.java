package com.teddycrane.racemanagement.utils.converter;

import com.teddycrane.racemanagement.enums.RacerSearchType;

public class RacerSearchTypeConverter extends StringToEnumConverter<RacerSearchType> {

  @Override
  public RacerSearchType convert(String source) {
    try {
      return RacerSearchType.valueOf(source.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
