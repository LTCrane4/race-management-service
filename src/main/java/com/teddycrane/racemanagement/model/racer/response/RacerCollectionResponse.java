package com.teddycrane.racemanagement.model.racer.response;

import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.racer.RacerDto;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RacerCollectionResponse implements Response {
  private Collection<RacerDto> racers;
}
