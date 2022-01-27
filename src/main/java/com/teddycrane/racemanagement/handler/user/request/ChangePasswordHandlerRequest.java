package com.teddycrane.racemanagement.handler.user.request;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChangePasswordHandlerRequest {

  private UUID id;

  private String oldPassword, newPassword;
}
