package com.teddycrane.racemanagement.model.user.response;

import com.teddycrane.racemanagement.model.user.User;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class UserCollectionResponse {
  private final Collection<UserResponse> users;

  public UserCollectionResponse(@NonNull Collection<User> users) {
    this.users = new ArrayList<>();
    users.forEach((user) -> this.users.add(new UserResponse(user)));
  }
}
