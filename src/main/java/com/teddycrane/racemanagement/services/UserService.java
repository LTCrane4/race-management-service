package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotAuthorizedException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.model.request.AuthenticationRequest;
import com.teddycrane.racemanagement.model.response.AuthenticationResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  Optional<User> getUser(UUID id);

  User createUser(String username, String password, String firstName,
                  String lastName, String email, UserType userType)
      throws DuplicateItemException;

  User updateUser(UUID id, String firstName, String lastName, String email,
                  UserType userType) throws NotFoundException;

  AuthenticationResponse login(String username, String password)
      throws NotAuthorizedException;
}
