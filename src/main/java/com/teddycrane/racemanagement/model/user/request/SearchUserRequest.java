package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchUserRequest {
  private String userId;
  private String firstName, lastName, username;
  private UserType userType;

  public boolean isValidRequest() {
    return userId != null
        || firstName != null
        || lastName != null
        || username != null
        || userType != null;
  }
}
