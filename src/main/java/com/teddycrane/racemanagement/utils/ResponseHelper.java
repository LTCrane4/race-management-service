package com.teddycrane.racemanagement.utils;

import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseHelper {
  public static ResponseEntity<ErrorResponse> createErrorResponse(
      String message, HttpStatus status) {
    return ResponseEntity.status(status).body(new ErrorResponse(message));
  }

  public static <T extends Response> ResponseEntity<T> createSuccessResponse(T body) {
    return ResponseEntity.ok(body);
  }
}
