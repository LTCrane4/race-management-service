package com.teddycrane.racemanagement.model.request;

import com.teddycrane.racemanagement.enums.UserType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CreateUserRequest {

  @NotBlank(message = "A username must be provided") private String username;

  @NotBlank(message = "A password must be provided") private String password;

  @NotBlank(message = "A first and last name must both be provided")
  private String firstName, lastName;

  @Email private String email;

  private UserType userType;

  public CreateUserRequest() {}

  public CreateUserRequest(String username, String password, String firstName,
                           String lastName, String email) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.userType = UserType.USER;
  }

  public CreateUserRequest(String username, String password, String firstName,
                           String lastName, String email, UserType userType) {
    this(username, password, firstName, lastName, email);
    this.userType = userType;
  }

  public String getUsername() { return username; }

  public String getPassword() { return password; }

  public String getFirstName() { return firstName; }

  public String getLastName() { return lastName; }

  public String getEmail() { return email; }

  public UserType getUserType() { return userType; }
}
