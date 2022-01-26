package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.handler.Handler;
import com.teddycrane.racemanagement.handler.user.request.DeleteUserRequest;
import com.teddycrane.racemanagement.handler.user.request.UpdateUserHandlerRequest;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
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
  private final Handler<UpdateUserHandlerRequest, User> updateUserHandler;
  private final Handler<DeleteUserRequest, User> deleteUserHandler;

  public UserServiceImpl(
      UserRepository userRepository,
      TokenManager tokenManager,
      AuthenticationManager authenticationManager,
      Handler<UUID, User> getUserHandler,
      Handler<String, Collection<User>> getUsersHandler,
      Handler<CreateUserRequest, User> createUserHandler,
      Handler<UpdateUserHandlerRequest, User> updateUserHandler,
      Handler<DeleteUserRequest, User> deleteUserHandler) {
    super();
    this.userRepository = userRepository;
    this.tokenManager = tokenManager;
    this.authenticationManager = authenticationManager;
    this.getUserHandler = getUserHandler;
    this.getUsersHandler = getUsersHandler;
    this.createUserHandler = createUserHandler;
    this.updateUserHandler = updateUserHandler;
    this.deleteUserHandler = deleteUserHandler;
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
  public User updateUser(UUID id, UpdateUserRequest updateRequest) throws NotFoundException {
    logger.info("updateUser called");

    UpdateUserHandlerRequest request = new UpdateUserHandlerRequest(updateRequest, id);

    return this.updateUserHandler.resolve(request);
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
  public boolean changePassword(UUID id, String oldPassword, String newPassword)
      throws NotFoundException {
    logger.info("changePassword called");

    User user =
        this.userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("No user found for the provided id"));

    String encodedPassword = this.encodePassword(oldPassword);
    if (encodedPassword.equals(user.getPassword())) {
      user.setPassword(this.encodePassword(newPassword));
      return true;
    } else {
      logger.error("Incorrect old password. Please try again");
      throw new BadRequestException("Previous password provided was incorrect.  Please try again.");
    }
  }

  @Override
  public User deleteUser(UUID id) throws NotFoundException {
    logger.info("deleteUser called for {} by {}", id, "test");

    return this.deleteUserHandler.resolve(new DeleteUserRequest(id));
  }
}
