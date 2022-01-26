package com.teddycrane.racemanagement.handler.user.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteUserRequest {
  private UUID id;
}
