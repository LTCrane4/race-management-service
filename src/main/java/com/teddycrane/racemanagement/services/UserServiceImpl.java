package com.teddycrane.racemanagement.services;

import static com.teddycrane.racemanagement.utils.PasswordEncoder.encodePassword;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.BadRequestException;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.InternalServerError;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.security.util.TokenManager;
import com.teddycrane.racemanagement.utils.PasswordEncoder;
import java.time.Instant;
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
    return this.userRepository.findAll();
  }

  @Override
  public User getUser(UUID id) throws NotFoundException {
    logger.info("getUser called");
    return this.userRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("No user found for the provided id"));
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

    Optional<User> existing = this.userRepository.findByUsername(request.getUsername());

    if (existing.isPresent()) {
      throw new DuplicateItemException(
          "This username is already taken.  Please try a different username");
    }

    UserType type = request.getUserType() == null ? UserType.USER : request.getUserType();

    return this.userRepository.save(
        new User(
            request.getFirstName(),
            request.getLastName(),
            request.getUsername(),
            request.getEmail(),
            encodePassword(request.getPassword()),
            type));
  }

  @Override
  public User updateUser(
      UUID id,
      String firstName,
      String lastName,
      String email,
      UserType userType,
      Instant updatedTimestamp)
      throws ConflictException, NotFoundException, InternalServerError {
    logger.info("updateUser called for user id {}", id);

    // track if the update operation succeeded
    boolean isUpdated = false;
    User existing =
        this.userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("No user found for the provided id"));

    // check updated timestamp
    if (!updatedTimestamp.equals(existing.getUpdatedTimestamp())) {
      throw new ConflictException("Conflict: Please re-fetch data and attempt update again");
    }

    // update actions
    if (firstName != null) {
      existing.setFirstName(firstName);
      isUpdated = true;
    }
    if (lastName != null) {
      existing.setLastName(lastName);
      isUpdated = true;
    }
    if (email != null) {
      existing.setEmail(email);
      isUpdated = true;
    }
    if (userType != null) {
      existing.setUserType(userType);
      isUpdated = true;
    }

    if (isUpdated) {
      existing.setUpdatedTimestamp(Instant.now());
      return this.userRepository.save(existing);
    } else {
      throw new InternalServerError();
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
  public boolean changePassword(UUID id, String oldPassword, String newPassword)
      throws BadRequestException, NotFoundException {
    logger.info("changePassword called");
    User user =
        this.userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("No user found for the provided id"));

    if (PasswordEncoder.validatePassword(user.getPassword(), oldPassword)) {
      user.setPassword(encodePassword(newPassword));
      this.userRepository.save(user);
      return true;
    } else {
      throw new BadRequestException("Previous password provided was incorrect.  Please try again.");
    }
  }

  @Override
  public User deleteUser(UUID id) throws NotFoundException {
    logger.info("deleteUser called for {} by {}", id, "test");
    User u =
        this.userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("No user found for the provided id"));

    this.userRepository.delete(u);
    return u;
  }
}
