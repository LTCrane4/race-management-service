package com.teddycrane.racemanagement.utils;

import com.teddycrane.racemanagement.model.response.ErrorResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

@UtilityClass
public class ResponseStatusGenerator {
  @NonNull
  private static ResponseEntity<ErrorResponse> generateResponse(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new ErrorResponse(message));
  }

  @NonNull
  public static ResponseEntity<ErrorResponse> generateNotFoundResponse(String message) {
    return generateResponse(HttpStatus.NOT_FOUND, message);
  }

  @NonNull
  public static ResponseEntity<ErrorResponse> generateBadRequestResponse(String message) {
    return generateResponse(HttpStatus.BAD_REQUEST, message);
  }

  public static ResponseEntity<ErrorResponse> generateConflictResponse(String message) {
    return generateResponse(HttpStatus.CONFLICT, message);
  }
}
