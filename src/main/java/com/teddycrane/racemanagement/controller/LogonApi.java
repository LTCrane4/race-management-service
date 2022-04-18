package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.user.request.AuthenticationRequest;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface LogonApi {
  @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
  @Operation(description = "Login user")
  ResponseEntity<? extends Response> login(@Valid @RequestBody AuthenticationRequest request);
}
