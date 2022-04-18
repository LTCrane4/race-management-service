package com.teddycrane.racemanagement.model.user.response;

import com.teddycrane.racemanagement.model.Response;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthenticationResponse implements Response {
  private String token;
}
