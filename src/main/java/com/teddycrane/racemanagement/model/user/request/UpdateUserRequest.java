package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserType;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

  private String firstName, lastName;

  @Email private String email;

  private UserType userType;
}
