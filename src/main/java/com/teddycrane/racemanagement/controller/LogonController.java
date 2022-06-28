package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.services.UserService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogonController extends BaseController implements LogonApi {

  private final UserService userService;

  public LogonController(UserService userService) {
    super();
    this.userService = userService;
  }

  @Override
  public ResponseEntity<AuthenticationResponse> login(@NonNull Map<String, String> request)
      throws BadRequestException, NotAuthorizedException {
    logger.info("Login requested");

    // validate request
    if (!request.containsKey("username") || !request.containsKey("password")) {
      throw new BadRequestException("Required parameters not provided");
    }

    String username = request.get("username");
    String password = request.get("password");

    try {
      return ResponseEntity.ok().body(this.userService.login(username, password));
    } catch (NotAuthorizedException e) {
      logger.warn("User is not authorized");
      throw new NotAuthorizedException("Unauthorized");
    }
  }
}
