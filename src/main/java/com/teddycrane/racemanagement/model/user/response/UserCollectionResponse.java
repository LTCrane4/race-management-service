package com.teddycrane.racemanagement.model.user.response;

import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.user.dto.UserDTO;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@NoArgsConstructor
public class UserCollectionResponse implements Response {
  private Collection<UserDTO> users;

  public UserCollectionResponse(@NonNull Collection<UserDTO> users) {
    this.users = new ArrayList<>();
    this.users.addAll(users);
  }
}
