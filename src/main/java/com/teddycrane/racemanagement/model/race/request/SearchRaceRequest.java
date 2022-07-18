package com.teddycrane.racemanagement.model.race.request;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.model.request.SearchRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRaceRequest implements SearchRequest {
  private String name;
  private Category category;

  @Generated
  @Override
  public boolean isValidRequest() {
    return name != null || category != null;
  }
}
