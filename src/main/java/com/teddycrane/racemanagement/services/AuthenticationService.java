package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.User;
import com.teddycrane.racemanagement.model.UserPrincipal;
import com.teddycrane.racemanagement.repositories.UserRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService
    extends BaseService implements UserDetailsService {

  private final UserRepository userRepository;

  @Value("${users.test-user.username}") private String testUsername;

  @Value("${users.test-user.password}") private String testUserPassword;

  //  @Value("${users.test-admin.username}") private String testAdmin;
  //  @Value("${users.test-admin.password}") private String testAdminPassword;

  public AuthenticationService(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  @PostConstruct
  public void initialize() {
    if (this.userRepository.findByUsername("testuser").orElse(null) == null) {
      this.userRepository.save(new User("Test", "User", testUsername,
                                        "testuser@teddycrane.com",
                                        testUserPassword));
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    logger.info("loadByUsername called: {}", username);
    User user = this.userRepository.findOneByUsername(username);
    return new UserPrincipal(user);
  }
}
