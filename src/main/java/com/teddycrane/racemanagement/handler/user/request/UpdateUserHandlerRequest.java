package com.teddycrane.racemanagement.handler.user.request;

import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserHandlerRequest extends UpdateUserRequest {

  private UUID requesterId;

  public UpdateUserHandlerRequest(UpdateUserRequest request, UUID requesterId) {
    super(request);
    this.requesterId = requesterId;
  }
}
