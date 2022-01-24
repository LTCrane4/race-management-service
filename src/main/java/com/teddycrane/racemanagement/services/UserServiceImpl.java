package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.security.util.TokenManager;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseService implements UserService {

  private final UserRepository userRepository;

  private final TokenManager tokenManager;

  private final AuthenticationManager authenticationManager;

  private final Handler<UUID, User> getUserHandler;
  private final Handler<String, Collection<User>> getUsersHandler;
  private final Handler<CreateUserRequest, User> createUserHandler;

  public UserServiceImpl(
      UserRepository userRepository,
      TokenManager tokenManager,
      AuthenticationManager authenticationManager,
      Handler<UUID, User> getUserHandler,
      Handler<String, Collection<User>> getUsersHandler,
      Handler<CreateUserRequest, User> createUserHandler) {
    super();
    this.userRepository = userRepository;
    this.tokenManager = tokenManager;
    this.authenticationManager = authenticationManager;
    this.getUserHandler = getUserHandler;
    this.getUsersHandler = getUsersHandler;
    this.createUserHandler = createUserHandler;
  }

  @Override
  public Collection<User> getAllUsers() {
    logger.info("getAllUsers called");
    return this.getUsersHandler.resolve("");
  }

  @Override
  public User getUser(UUID id) throws NotFoundException {
    logger.info("getUser called");
    User u = this.getUserHandler.resolve(id);

    if (u == null) {
      logger.error("No user found for the id {}", id);
      throw new NotFoundException("No user found for the provided id!");
    }

    return u;
  }

  @Override
  public Collection<User> searchUsers(@NonNull SearchType searchType, String searchValue)
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

  @Override
  public User createUser(@NonNull CreateUserRequest request) throws DuplicateItemException {
    logger.info("createUser called");

    // set type to USER if there is no type provided
    UserType type = request.getUserType() == null ? UserType.USER : request.getUserType();
    request.setUserType(type);

    return this.createUserHandler.resolve(request);
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

  @Override
  public User deleteUser(UUID id) throws NotFoundException {
    logger.info("deleteUser called for {} by {}", id, "test");

    User u = this.userRepository.findById(id).orElse(null);

    if (u != null) {
      this.userRepository.delete(u);
      return u;
    } else {
      logger.error("No user found with the id {}", id);
      throw new NotFoundException("No user found to delete with the specified id");
    }
  }
}
