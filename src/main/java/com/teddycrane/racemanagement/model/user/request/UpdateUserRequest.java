package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserType;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

  private String firstName, lastName;

  @Email private String email;

  private UserType userType;

  protected UpdateUserRequest(@NonNull UpdateUserRequest other) {
    this(other.firstName, other.lastName, other.email, other.userType);
  }
}
