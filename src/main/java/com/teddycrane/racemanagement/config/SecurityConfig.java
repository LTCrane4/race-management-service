package com.teddycrane.racemanagement.config;

import com.teddycrane.racemanagement.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final UserService authenticationService;

  public SecurityConfig(UserService authenticationService) {
    this.authenticationService = authenticationService;
  }
}
