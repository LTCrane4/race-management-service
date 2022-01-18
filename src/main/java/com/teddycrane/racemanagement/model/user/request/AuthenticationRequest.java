package com.teddycrane.racemanagement.model.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
public class AuthenticationRequest {

  @NonNull private String username, password;
}
