package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.UserStatus;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import com.teddycrane.racemanagement.repositories.UserRepository;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.Generated;
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

  @Generated
  private void createSeedUserData() {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    User user =
        new User(
            UUID.fromString(testUserId),
            "Test",
            "User",
            testUsername,
            "testuser@test.fake",
            encoder.encode(testUserPassword),
            UserType.USER,
            UserStatus.ACTIVE);
    this.userRepository.save(user);

    User admin =
        new User(
            UUID.fromString(testAdminId),
            "Test",
            "Admin",
            testAdmin,
            "testadmin@test.fake",
            encoder.encode(testAdminPassword),
            UserType.ADMIN,
            UserStatus.ACTIVE);
    this.userRepository.save(admin);
  }

  @PostConstruct
  @Generated
  public void initialize() {
    // get the current profile
    String profile = System.getProperty("spring.profiles.active");
    if (profile != null && (profile.equals("local") || profile.equals("integration"))) {
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
