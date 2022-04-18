package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import java.util.Collection;
import org.springframework.stereotype.Service;

@Service
public interface UserSearchService {
  Collection<User> searchUsers(SearchUserRequest request);
}
