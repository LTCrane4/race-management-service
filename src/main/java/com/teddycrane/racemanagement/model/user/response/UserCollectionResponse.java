package com.teddycrane.racemanagement.model.user.response;

import com.teddycrane.racemanagement.model.user.User;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCollectionResponse {
  private Collection<User> users;
}