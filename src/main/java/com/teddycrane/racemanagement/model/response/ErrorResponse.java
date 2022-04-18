package com.teddycrane.racemanagement.model.response;

import com.teddycrane.racemanagement.model.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@SuppressWarnings("unused")
public class ErrorResponse implements Response {
  @Getter private String message;
}
