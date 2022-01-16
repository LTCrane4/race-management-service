package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.User;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  User getUser(UUID id);
}
