package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@Setter
@AllArgsConstructor
@Builder
public class SearchUserRequest {
  @Nullable private UUID userId;
  @Getter private String firstName, lastName, username;
  @Nullable private UserType userType;

  @Generated
  public String getUserType() {
    return userType != null ? userType.toString() : null;
  }

  @Generated
  public String getUserId() {
    return userId != null ? userId.toString() : null;
  }

  @Generated
  public boolean isValidRequest() {
    return userId != null
        || firstName != null
        || lastName != null
        || username != null
        || userType != null;
  }
}
