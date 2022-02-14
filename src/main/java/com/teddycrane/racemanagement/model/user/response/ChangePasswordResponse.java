package com.teddycrane.racemanagement.model.user.response;

import com.teddycrane.racemanagement.model.Response;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChangePasswordResponse implements Response {
  private boolean status;
  private UUID userId;
}
