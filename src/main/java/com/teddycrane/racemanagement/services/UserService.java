package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.User;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  User getUser(UUID id) throws NotFoundException;

  User createUser(String username, String password, String firstName,
                  String lastName, String email, UserType userType)
      throws DuplicateItemException;
}
