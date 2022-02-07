package com.teddycrane.racemanagement.model.response;

import com.teddycrane.racemanagement.model.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
@SuppressWarnings("unused")
public class ErrorResponse implements Response {
  private String message;
}
