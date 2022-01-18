package com.teddycrane.racemanagement.model.user.request;

import com.teddycrane.racemanagement.enums.UserType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateUserRequest {

  @NotBlank(message = "A username must be provided") private String username;

  @NotBlank(message = "A password must be provided") private String password;

  @NotBlank(message = "A first and last name must both be provided")
  private String firstName, lastName;

  @Email private String email;

  private UserType userType;

  public CreateUserRequest(String username, String password, String firstName,
                           String lastName, String email) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.userType = UserType.USER;
  }
}
