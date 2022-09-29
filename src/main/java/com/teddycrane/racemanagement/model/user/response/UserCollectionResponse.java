package com.teddycrane.racemanagement.model.user.response;

import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.user.UserDto;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@NoArgsConstructor
public class UserCollectionResponse implements Response {
  private Collection<UserDto> users;

  public UserCollectionResponse(@NonNull Collection<UserDto> users) {
    this.users = new ArrayList<>();
    this.users.addAll(users);
  }
}
