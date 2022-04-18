package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserStatus;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeStatusRequest {
  @NotBlank(message = "A valid user status must be provided")
  private UserStatus status;

  @NotBlank(message = "An updated timestamp must be provided")
  private String updatedTimestamp;
}
