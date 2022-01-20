package com.teddycrane.racemanagement.model.user;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

  @Getter private User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    if (this.user != null) {
      return this.user.getPassword();
    } else {
      return null;
    }
  }

  @Override
  public String getUsername() {
    if (this.user != null) {
      return this.user.getUsername();
    } else {
      return null;
    }
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO update this when account expiry is complete
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO update when account expiry is enabled
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO todo update when password expiry is enabled
    return true;
  }

  @Override
  public boolean isEnabled() {
    // TODO update when account enable/disable is implemented
    return true;
  }
}
