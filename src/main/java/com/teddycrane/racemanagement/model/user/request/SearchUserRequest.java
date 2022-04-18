package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@AllArgsConstructor
@Builder
public class SearchUserRequest {
  private UUID userId;
  @Getter private String firstName, lastName, username;
  private UserType userType;

  public String getUserType() {
    return userType != null ? userType.toString() : null;
  }

  public String getUserId() {
    return userId != null ? userId.toString() : null;
  }

  public boolean isValidRequest() {
    return userId != null
        || firstName != null
        || lastName != null
        || username != null
        || userType != null;
  }
}
