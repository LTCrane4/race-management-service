package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

  private String firstName, lastName, email;

  private UserType userType;
}
