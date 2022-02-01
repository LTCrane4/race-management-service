package com.teddycrane.racemanagement.model.race.response;

import com.teddycrane.racemanagement.model.race.Race;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RaceCollectionResponse {
  private Collection<Race> data;
}
