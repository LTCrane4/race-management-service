package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService extends BaseService implements UserDetailsService {

  private final UserRepository userRepository;

  @Value("${users.test-user.username}")
  private String testUsername;

  @Value("${users.test-user.password}")
  private String testUserPassword;

  @Value("${users.test-user.id}")
  private String testUserId;

  @Value("${users.test-admin.username}")
  private String testAdmin;

  @Value("${users.test-admin.password}")
  private String testAdminPassword;

  @Value("${users.test-admin.id}")
  private String testAdminId;

  public AuthenticationService(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  private void createSeedUserData() {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    this.userRepository.save(
        User.builder()
            .id(UUID.fromString(testUserId))
            .username(testUsername)
            .password(encoder.encode(testUserPassword))
            .userType(UserType.USER)
            .email("testuser@test.fake")
            .firstName("Test")
            .lastName("User")
            .build());

    this.userRepository.save(
        User.builder()
            .id(UUID.fromString(testAdminId))
            .username(testAdmin)
            .password(encoder.encode(testAdminPassword))
            .userType(UserType.ADMIN)
            .email("testadmin@test.fake")
            .firstName("Test")
            .lastName("Admin")
            .build());
  }

  @PostConstruct
  public void initialize() {
    // get the current profile
    String profile = System.getProperty("spring.profiles.active");
    if (profile.equals("local") || profile.equals("integration")) {
      this.createSeedUserData();
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("loadByUsername called: {}", username);
    User user = this.userRepository.findOneByUsername(username);
    return new UserPrincipal(user);
  }
}
