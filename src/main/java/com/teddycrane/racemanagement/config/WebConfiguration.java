package com.teddycrane.racemanagement.config;

import com.teddycrane.racemanagement.utils.converter.RacerSearchTypeConverter;
import com.teddycrane.racemanagement.utils.converter.SearchTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new SearchTypeConverter());
    registry.addConverter(new RacerSearchTypeConverter());
  }
}
