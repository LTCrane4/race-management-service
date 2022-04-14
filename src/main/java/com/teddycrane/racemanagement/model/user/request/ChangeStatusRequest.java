package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserStatus;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeStatusRequest {
  @NotNull private UserStatus status;

  @NotNull private Instant updatedTimestamp;
}
