package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.model.UserPrincipal;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl
    extends BaseService implements UserService, UserDetailsService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> getUser(UUID id) {
    logger.trace("getUser called");
    return this.userRepository.findById(id);
  }

  @Override
  public User createUser(String username, String password, String firstName,
                         String lastName, String email, UserType userType)
      throws DuplicateItemException {
    Optional<User> existing = this.userRepository.findByUsername(username);

    if (existing.isPresent()) {
      logger.error("A user with the same username already exists! ");
      throw new DuplicateItemException(
          "This username is already taken.  Please try a different username");
    }

    return this.userRepository.save(
        new User(firstName, lastName, username, email, password, userType));
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    Optional<User> _user = this.userRepository.findByUsername(username);

    if (_user.isPresent()) {
      UserPrincipal principal = new UserPrincipal(_user.get());
      return principal;
    } else {
      logger.warn("No user found for the provided username");
      throw new UsernameNotFoundException(
          "No user found for the provided username");
    }
  }
}
