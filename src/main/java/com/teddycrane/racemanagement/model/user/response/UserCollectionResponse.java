package com.teddycrane.racemanagement.model.user.response;

import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.user.User;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@NoArgsConstructor
public class UserCollectionResponse implements Response {
  private Collection<UserResponse> users;

  public UserCollectionResponse(@NonNull Collection<User> users) {
    this.users = new ArrayList<>();
    users.forEach((user) -> this.users.add(new UserResponse(user)));
  }
}
