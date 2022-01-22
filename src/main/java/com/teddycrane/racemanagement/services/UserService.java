package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.response.AuthenticationResponse;
import java.util.Collection;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  Collection<User> getAllUsers();

  User getUser(UUID id) throws NotFoundException;

  Collection<User> searchUsers(SearchType searchType, String searchValue)
      throws IllegalArgumentException;

  User createUser(
      String username,
      String password,
      String firstName,
      String lastName,
      String email,
      UserType userType)
      throws DuplicateItemException;

  User updateUser(UUID id, String firstName, String lastName, String email, UserType userType)
      throws NotFoundException;

  AuthenticationResponse login(String username, String password) throws NotAuthorizedException;

  boolean changePassword(UUID id, String oldPassword, String newPassword) throws NotFoundException;

  User deleteUser(UUID id) throws NotFoundException;
}
