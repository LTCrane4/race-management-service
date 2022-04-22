package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.response.ErrorResponse;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

/** A parent class for all controllers in the project, to allow reuse of logger boilerplate. */
@RestController
public abstract class BaseController {

  protected static final String CREATE = "create";
  protected static final String UPDATE = "update";
  protected static final String DELETE = "delete";

  protected final Logger logger = LogManager.getLogger(this.getClass());

  /**
   * Retrieves the relevant User audit data from the security context.
   *
   * @return The user audit data needed for gating and audit logging.
   */
  protected UserAuditData getUserAuditData() {
    var principal =
        ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getUser();

    return new UserAuditData(
        principal.getUserType(),
        principal.getUsername(),
        principal.getId(),
        principal.getUserType());
  }

  /**
   * Prints an audit log for the given parameters.
   *
   * @param action The action requested by the user.
   * @param id The id of the requesting user.
   * @param role The role held by the requesting user.
   */
  protected void printAuditLog(String action, UUID id, UserType role) {
    logger.info("{} requested by {} with role {}", action, id, role);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @EqualsAndHashCode
  static class UserAuditData {

    private UserType type;
    private String userName;
    private UUID userId;
    private UserType userType;
  }

  // TODO fully remove this method
  @Deprecated
  protected ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status) {
    return new ResponseEntity<ErrorResponse>(new ErrorResponse(message), status);
  }
}
