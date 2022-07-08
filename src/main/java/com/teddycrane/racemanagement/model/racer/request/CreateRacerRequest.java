package com.teddycrane.racemanagement.model.racer.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@Data
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

  @NonNull private String category;

  private String middleName, teamName, phoneNumber;

  @Email private String email;

  private int bibNumber;
}
