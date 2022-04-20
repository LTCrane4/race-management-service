package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Collection;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UserSearchServiceImpl extends BaseService implements UserSearchService {

  private final UserRepository userRepository;

  public UserSearchServiceImpl(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  @Override
  public Collection<User> searchUsers(@NonNull SearchUserRequest request) {
    logger.info("searchUsers called");
    return this.userRepository.searchUsers(
        request.getUserId(),
        request.getFirstName(),
        request.getLastName(),
        request.getUsername(),
        request.getUserType());
  }
}
