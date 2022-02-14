package com.teddycrane.racemanagement.model.user.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChangePasswordRequest {

  @NotBlank(message = "Old Password cannot be blank")
  private String oldPassword;

  @NotBlank(message = "New password cannot be blank")
  private String newPassword;
}
