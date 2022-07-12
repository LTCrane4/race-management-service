package com.teddycrane.racemanagement.model.response;

import com.teddycrane.racemanagement.model.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GenericResponse implements Response {
  private String message;
}
