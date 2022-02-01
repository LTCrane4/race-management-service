package com.teddycrane.racemanagement.model.race.request;

import com.teddycrane.racemanagement.enums.Category;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateRaceRequest {
  @NotBlank(message = "The race name cannot be blank")
  private String name;

  private Category category;

  private List<UUID> racerIds;
}
