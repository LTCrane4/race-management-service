package com.teddycrane.racemanagement.model.race.request;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AddRacersRequest {
  @NotBlank(message = "Updated timestamp cannot be blank")
  private String updatedTimestamp;

  @NotEmpty private List<UUID> racerIds;
}
