package com.teddycrane.racemanagement.model.racer.request;

import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateRacerRequest {
  private String firstName, lastName, middleName, teamName, phoneNumber;

  @Email private String email;

  public boolean allParamsNull() {
    return this.firstName == null
        && this.lastName == null
        && this.middleName == null
        && this.teamName == null
        && this.phoneNumber == null
        && this.email == null;
  }
}
