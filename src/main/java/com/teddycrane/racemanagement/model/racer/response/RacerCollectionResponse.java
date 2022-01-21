package com.teddycrane.racemanagement.model.racer.response;

import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RacerCollectionResponse {
  private Collection<Racer> racers;
}
