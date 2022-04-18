package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import com.teddycrane.racemanagement.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController extends BaseController implements LogonApi {

  private final UserService userService;

  public LoginController(UserService userService) {
    super();
    this.userService = userService;
  }

  @Override
  public ResponseEntity<? extends Response> login(@NonNull AuthenticationRequest request) {
    logger.info("Login requested");

    try {
      return ResponseEntity.ok()
          .body(this.userService.login(request.getUsername(), request.getPassword()));
    } catch (NotAuthorizedException e) {
      logger.warn("User is not authorized");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
