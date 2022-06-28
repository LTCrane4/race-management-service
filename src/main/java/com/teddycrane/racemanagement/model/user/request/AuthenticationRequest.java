package com.teddycrane.racemanagement.model.user.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class AuthenticationRequest {

  @NotBlank(message = "username and password must both not be blank!")
  private String username, password;
}
