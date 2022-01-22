package com.teddycrane.racemanagement.model.user.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordRequest {
  @NotNull private String oldPassword;

  @NotNull private String newPassword;
}
