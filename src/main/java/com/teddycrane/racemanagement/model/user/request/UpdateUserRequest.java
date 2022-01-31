package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

  // This is a string from the request and is validated in the controller
  @NotBlank(message = "The updated timestamp must be included in the request")
  private String updatedTimestamp;

  protected UpdateUserRequest(@NonNull UpdateUserRequest other) {
    this(other.firstName, other.lastName, other.email, other.userType, other.updatedTimestamp);
  }
}
