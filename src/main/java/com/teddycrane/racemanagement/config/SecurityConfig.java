package com.teddycrane.racemanagement.config;

import com.teddycrane.racemanagement.security.JwtAuthenticationEntryPoint;
import com.teddycrane.racemanagement.security.filter.AuthenticationFilter;
import com.teddycrane.racemanagement.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final AuthenticationService authenticationService;

  private final JwtAuthenticationEntryPoint authenticationEntryPoint;

  private final AuthenticationFilter filter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public SecurityConfig(
      AuthenticationService authenticationService,
      JwtAuthenticationEntryPoint authenticationEntryPoint,
      AuthenticationFilter filter) {
    this.authenticationService = authenticationService;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.filter = filter;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/login", "/actuator/*", "/v3/**", "/swagger-ui.html", "/swagger-ui/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.eraseCredentials(true)
        .userDetailsService(this.authenticationService)
        .passwordEncoder(passwordEncoder());
  }
}
