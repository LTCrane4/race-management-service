package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.security.util.TokenManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseService implements UserService {

  private final UserRepository userRepository;

  private final TokenManager tokenManager;

  private final AuthenticationManager authenticationManager;

  public UserServiceImpl(
      UserRepository userRepository,
      TokenManager tokenManager,
      AuthenticationManager authenticationManager) {
    super();
    this.userRepository = userRepository;
    this.tokenManager = tokenManager;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Collection<User> getAllUsers() {
    logger.info("getAllUsers called");
    return new ArrayList<>(this.userRepository.findAll());
  }

  @Override
  public Optional<User> getUser(UUID id) {
    logger.info("getUser called");
    return this.userRepository.findById(id);
  }

  @Override
  public Collection<User> searchUsers(SearchType searchType, String searchValue)
      throws IllegalArgumentException {
    logger.info("searchUsers called");

    switch (searchType) {
      case TYPE:
        return this.userRepository.findAllByUserType(UserType.valueOf(searchValue.toUpperCase()));
      case NAME:
      default:
        return this.userRepository.findAllByLastName(searchValue);
    }
  }

  private String encodePassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }

  @Override
  public User createUser(
      String username,
      String password,
      String firstName,
      String lastName,
      String email,
      UserType userType)
      throws DuplicateItemException {
    logger.info("createUser called");
    Optional<User> existing = this.userRepository.findByUsername(username);

    if (existing.isPresent()) {
      logger.error("A user with the same username already exists! ");
      throw new DuplicateItemException(
          "This username is already taken.  Please try a different username");
    }

    // If userType is not present or null, set user type to user
    UserType type = userType == null ? UserType.USER : userType;

    return this.userRepository.save(
        new User(firstName, lastName, username, email, encodePassword(password), type));
  }

  @Override
  public User updateUser(
      UUID id, String firstName, String lastName, String email, UserType userType)
      throws NotFoundException {
    logger.info("updateUser called");
    Optional<User> existing = this.userRepository.findById(id);

    if (existing.isPresent()) {
      User user = existing.get();
      if (firstName != null) {
        user.setFirstName(firstName);
      }
      if (lastName != null) {
        user.setLastName(lastName);
      }
      if (email != null) {
        user.setEmail(email);
      }
      // todo update so that non-admin/root users can't update the user type
      if (userType != null) {
        user.setUserType(userType);
      }

      return this.userRepository.save(user);
    } else {
      logger.error("No user found for the id {}", id);
      throw new NotFoundException("No user found for the provided id");
    }
  }

  @Override
  public AuthenticationResponse login(String username, String password)
      throws NotAuthorizedException {
    logger.info("login called");
    String token;

    try {
      this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));

      Optional<User> user = this.userRepository.findByUsername(username);
      if (user.isPresent()) {
        token = this.tokenManager.generateToken(new UserPrincipal(user.get()));
        return new AuthenticationResponse(token);
      } else {
        throw new UsernameNotFoundException("No user found for the provided username");
      }
    } catch (AuthenticationException | NotFoundException e) {
      logger.warn("Unable to authenticate with the provided credentials");
      throw new NotAuthorizedException("Unauthorized");
    }
  }
}
