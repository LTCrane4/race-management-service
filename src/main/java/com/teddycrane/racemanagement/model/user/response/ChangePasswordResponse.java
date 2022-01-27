package com.teddycrane.racemanagement.model.user.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChangePasswordResponse {
  private boolean status;
  private UUID userId;
}
