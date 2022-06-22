package com.teddycrane.racemanagement.model.racer.request;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.model.request.SearchRequest;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRacerRequest implements SearchRequest {
  private String firstName, middleName, lastName, teamName;
  private Category category;
  private Boolean isDeleted;

  @Override
  public boolean isValidRequest() {
    return firstName != null
        || lastName != null
        || middleName != null
        || teamName != null
        || category != null
        || Optional.ofNullable(isDeleted).orElse(false);
  }
}
