package com.teddycrane.racemanagement.config;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.ForbiddenException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.error.TransitionNotAllowedException;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@NoArgsConstructor
public class GlobalExceptionHandler {

  private final Logger logger = LogManager.getLogger(this.getClass());

  @ExceptionHandler({
    BadRequestException.class,
    ConflictException.class,
    DuplicateItemException.class,
    ForbiddenException.class,
    NotFoundException.class,
    TransitionNotAllowedException.class
  })
  public final ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();

    if (ex instanceof NotFoundException) {
      return this.handleNotFoundException((NotFoundException) ex, request, headers);
    }

    if (ex instanceof BadRequestException) {
      return this.handleBadRequest((BadRequestException) ex, request, headers);
    }

    if (ex instanceof DuplicateItemException) {
      return this.handleDuplicateItem((DuplicateItemException) ex, request, headers);
    }

    if (ex instanceof ForbiddenException) {
      return this.handleForbiddenException((ForbiddenException) ex, request, headers);
    }

    if (ex instanceof TransitionNotAllowedException) {
      return this.handleTransitionNotAllowed((TransitionNotAllowedException) ex, request, headers);
    }

    if (ex instanceof ConflictException) {
      return this.handleConflictException((ConflictException) ex, request, headers);
    }

    return new ResponseEntity<>(
        new ErrorResponse("Internal server error"), headers, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ErrorResponse> handleBadRequest(
      BadRequestException ex, WebRequest request, HttpHeaders headers) {
    String message = ex.getMessage() != null ? ex.getMessage() : "Bad Request";

    return new ResponseEntity<>(new ErrorResponse(message), headers, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorResponse> handleConflictException(
      ConflictException ex, WebRequest request, HttpHeaders headers) {
    String message = ex.getMessage() != null ? ex.getMessage() : "Conflict";

    return new ResponseEntity<>(new ErrorResponse(message), headers, HttpStatus.CONFLICT);
  }

  private ResponseEntity<ErrorResponse> handleDuplicateItem(
      DuplicateItemException ex, WebRequest request, HttpHeaders headers) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), headers, HttpStatus.CONFLICT);
  }

  private ResponseEntity<ErrorResponse> handleForbiddenException(
      ForbiddenException ex, WebRequest request, HttpHeaders headers) {

    logger.warn("Unauthorized access caught for user");
    return new ResponseEntity<>(new ErrorResponse("Forbidden"), headers, HttpStatus.FORBIDDEN);
  }

  private ResponseEntity<ErrorResponse> handleNotFoundException(
      NotFoundException ex, WebRequest request, HttpHeaders headers) {
    String message =
        ex.getMessage() != null ? ex.getMessage() : "Unable to find the specified resource";

    return new ResponseEntity<>(new ErrorResponse(message), headers, HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<ErrorResponse> handleTransitionNotAllowed(
      TransitionNotAllowedException ex, WebRequest request, HttpHeaders headers) {
    String message = ex.getMessage() != null ? ex.getMessage() : "Method Not Allowed";

    return new ResponseEntity<>(new ErrorResponse(message), headers, HttpStatus.METHOD_NOT_ALLOWED);
  }
}
