package com.teddycrane.racemanagement.config.converter;

import org.springframework.core.convert.converter.Converter;

public abstract class StringToEnumConverter<T extends Enum<?>>
    implements Converter<String, T> {

  @Override public abstract T convert(String source);
}
