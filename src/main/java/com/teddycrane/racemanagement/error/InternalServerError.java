package com.teddycrane.racemanagement.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalServerError extends ResponseStatusException {
  public InternalServerError() {
    super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
  }
}
