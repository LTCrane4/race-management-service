package com.teddycrane.racemanagement.model.racer.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRacerRequest {
  @NotBlank(message = "The id cannot be blank")
  private String id;

  @NotBlank(message = "The updated timestamp cannot be blank")
  private long updatedTimestamp;
}
