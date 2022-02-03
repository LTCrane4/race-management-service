package com.teddycrane.racemanagement.model.race.request;

import com.teddycrane.racemanagement.enums.Category;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateRaceRequest {

  @NotBlank(message = "The updated timestamp cannot be blank")
  private String updatedTimestamp;

  private String name;
  private Category category;
}
