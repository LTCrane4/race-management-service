package com.teddycrane.racemanagement.model.race.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PlaceRacerRequest {
  @NotBlank(message = "Updated timestamp must not be blank")
  private String updatedTimestamp;

  @NotBlank(message = "Racer ids must not be blank")
  private List<String> racerIds;
}
