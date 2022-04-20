package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserStatus;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.*;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  // TODO get this to filter based on status, optionally
  /**
   * Finds all users.
   *
   * @return A Collection of all users in the database.
   */
  Collection<User> getAllUsers();

  /**
   * Finds a single user by id.
   *
   * @param id The always valid UUID of the user. Invalid UUIDs should be caught by the controller.
   * @return A User corresponding to the provided id.
   * @throws NotFoundException Thrown if no user is found for the provided id.
   */
  User getUser(UUID id) throws NotFoundException;

  /**
   * Searches for users.
   *
   * @deprecated This method is deprecated and should not be used for new development.
   * @param searchType The search type.
   * @param searchValue The Search Value
   * @return A collection of users matching the search parameters.
   * @throws IllegalArgumentException thrown if one of the parameters is invalid.
   */
  @Deprecated
  Collection<User> searchUsers(SearchType searchType, String searchValue)
      throws IllegalArgumentException;

  User createUser(CreateUserRequest request) throws DuplicateItemException;

  User updateUser(
      UUID id,
      String firstName,
      String lastName,
      String email,
      UserType userType,
      Instant updatedTimestamp)
      throws ConflictException, InternalServerError, NotFoundException;

  AuthenticationResponse login(String username, String password) throws NotAuthorizedException;

  boolean changePassword(UUID id, String oldPassword, String newPassword) throws NotFoundException;

  User deleteUser(UUID id) throws NotFoundException;

  User deleteUserNew(UUID id) throws NotFoundException;

  User changeStatus(UUID id, UserStatus status, Instant updatedTimestamp)
      throws ConflictException, NotFoundException, TransitionNotAllowedException;
}
