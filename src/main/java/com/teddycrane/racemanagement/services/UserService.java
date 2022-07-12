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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  // TODO get this to filter based on status, optionally
  /**
   * Finds all users.
   *
   * @return A {@code Collection} of all users in the database.
   */
  Collection<User> getAllUsers();

  /**
   * Finds a single user by id.
   *
   * @param id The user {@code id}.
   * @return A User corresponding to the provided id.
   * @throws NotFoundException Thrown if no user is found for the provided id.
   */
  User getUser(@NonNull UUID id) throws NotFoundException;

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

  /**
   * Creates a new user
   *
   * @param request A Valid {@code CreateUserRequest}.
   * @return The newly created user
   * @throws DuplicateItemException Thrown if the user being added is a duplicate of an existing
   *     user.
   */
  User createUser(CreateUserRequest request) throws DuplicateItemException;

  /**
   * Updates a user
   *
   * @param id The ID of the user to be updated. Required.
   * @param updatedTimestamp The updated timestamp for the user. Required.
   * @param firstName The new first name for the user. Optional.
   * @param lastName The new last name for the user. Optional.
   * @param email The new email for the user. Optional.
   * @param userType The new user type for the user. Optional.
   * @return The updated user.
   * @throws ConflictException Thrown if the {@code updatedTimestamp} does not match the
   *     updatedTimestamp in the database.
   * @throws InternalServerError Thrown if the database operation fails.
   * @throws NotFoundException Thrown if no user is found for the provided {@code id}.
   */
  User updateUser(
      UUID id,
      Instant updatedTimestamp,
      @Nullable String firstName,
      @Nullable String lastName,
      @Nullable String email,
      @Nullable UserType userType)
      throws ConflictException, InternalServerError, NotFoundException;

  /**
   * Authenticates and generates a token for a login request.
   *
   * @param username The username for the user.
   * @param password The non-encrypted password for the user.
   * @return The {@code AuthenticationResponse} containing a new token for the user.
   * @throws NotAuthorizedException Thrown if the username/password combination is not valid.
   */
  AuthenticationResponse login(String username, String password) throws NotAuthorizedException;

  /**
   * Changes a user's password.
   *
   * @param id The user's id.
   * @param oldPassword The current (previous) password for the user.
   * @param newPassword The desired password for the user.
   * @return Returns true if the password is changed, otherwise, returns false and does not change
   *     the password.
   * @throws NotFoundException Thrown if no user is found for the provided id.
   */
  boolean changePassword(UUID id, String oldPassword, String newPassword) throws NotFoundException;

  User deleteUser(UUID id) throws NotFoundException;

  User deleteUserNew(UUID id) throws NotFoundException;

  User changeStatus(UUID id, UserStatus status, Instant updatedTimestamp)
      throws ConflictException, NotFoundException, TransitionNotAllowedException;
}
