package com.teddycrane.racemanagement.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TransitionNotAllowedException extends ResponseStatusException {
  public TransitionNotAllowedException(String message) {
    super(HttpStatus.METHOD_NOT_ALLOWED, message);
  }
}
