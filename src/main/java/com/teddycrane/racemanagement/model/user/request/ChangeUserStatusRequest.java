package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserStatus;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ChangeUserStatusRequest {
  @NotBlank(message = "An updated timestamp must be provided")
  private String updatedTimestamp;

  @NotBlank(message = "A user status must be provided")
  private UserStatus userStatus;
}
