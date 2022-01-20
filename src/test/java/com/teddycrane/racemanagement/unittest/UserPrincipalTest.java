package com.teddycrane.racemanagement.unittest;

import static org.junit.jupiter.api.Assertions.*;

import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserPrincipalTest {

  private UserPrincipal userPrincipal;

  @BeforeEach
  public void init() {
    this.userPrincipal = new UserPrincipal(TestResourceGenerator.generateUser());
  }

  @Test
  public void userPrincipalShouldConstruct() {
    assertNotNull(this.userPrincipal);
    assertNotNull(this.userPrincipal.getUser());
  }

  @Test
  public void userPrincipalShouldGet() {
    assertNull(userPrincipal.getAuthorities());
    assertNotNull(userPrincipal.getPassword());
    assertNotNull(userPrincipal.getUsername());
    assertTrue(userPrincipal.isAccountNonExpired());
    assertTrue(userPrincipal.isAccountNonLocked());
    assertTrue(userPrincipal.isEnabled());
    assertTrue(userPrincipal.isCredentialsNonExpired());
  }

  @Test
  public void userPrincipalWhenUserIsNull() {
    this.userPrincipal = new UserPrincipal(null);

    assertNull(userPrincipal.getUsername());
    assertNull(userPrincipal.getPassword());
  }
}
