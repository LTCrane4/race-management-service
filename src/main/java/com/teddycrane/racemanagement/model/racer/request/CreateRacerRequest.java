package com.teddycrane.racemanagement.model.racer.request;

import com.teddycrane.racemanagement.enums.Category;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class CreateRacerRequest {

  @NonNull
  @NotBlank(message = "A first name must be provided")
  private String firstName;

  @NonNull
  @NotBlank(message = "A last name must be provided")
  private String lastName;

  @NonNull
  @NotBlank(message = "A category must be provided")
  private Category category;

  private String middleName, teamName, phoneNumber;

  @Email private String email;

  private int bibNumber;
}
