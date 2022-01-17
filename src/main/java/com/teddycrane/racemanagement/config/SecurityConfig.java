package com.teddycrane.racemanagement.config;

import com.teddycrane.racemanagement.services.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final UserServiceImpl authenticationService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public SecurityConfig(UserServiceImpl authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // TODO update the permitAll when we have working x-platform password
    // hashing
    http.authorizeRequests()
        .antMatchers("/users/**")
        .permitAll()
        .and()
        .httpBasic();

    http.csrf().disable().cors().disable();
    http.headers().frameOptions().disable();
  }

  @Override
  public void configure(final AuthenticationManagerBuilder auth)
      throws Exception {
    auth.eraseCredentials(true)
        .userDetailsService(this.authenticationService)
        .passwordEncoder(passwordEncoder());
  }
}
