package com.teddycrane.racemanagement.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientPermissionsException extends ResponseStatusException {
  public InsufficientPermissionsException() {
    super(HttpStatus.FORBIDDEN, "Unauthorized");
  }
}
