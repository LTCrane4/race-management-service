package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.model.UserPrincipal;
import com.teddycrane.racemanagement.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

  private final UserRepository userRepository;

  public AuthenticationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    User user = this.userRepository.findOneByUsername(username);
    return new UserPrincipal(user);
  }
}
