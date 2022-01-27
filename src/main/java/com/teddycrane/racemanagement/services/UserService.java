package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.SearchType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.CreateUserRequest;
import com.teddycrane.racemanagement.model.user.request.UpdateUserRequest;
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

  User createUser(CreateUserRequest request) throws DuplicateItemException;

  User updateUser(UUID id, UpdateUserRequest updateRequest) throws NotFoundException;

  AuthenticationResponse login(String username, String password) throws NotAuthorizedException;

  boolean changePassword(UUID id, String oldPassword, String newPassword) throws NotFoundException;

  User deleteUser(UUID id) throws NotFoundException;
}
