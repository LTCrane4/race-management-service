package com.teddycrane.racemanagement.filter;

import com.teddycrane.racemanagement.services.UserService;
import com.teddycrane.racemanagement.services.UserServiceImpl;
import com.teddycrane.racemanagement.util.TokenManager;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private final Logger logger;
  private final UserServiceImpl userService;

  private final TokenManager tokenManager;

  public AuthenticationFilter(UserServiceImpl userService,
                              TokenManager tokenManager) {
    this.logger = LogManager.getLogger(this.getClass());
    this.userService = userService;
    this.tokenManager = tokenManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {
    logger.info("doFilterInternal called");
    String tokenHeader = request.getHeader("Authorization");
    String username = null;
    String token = null;

    if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
      token = tokenHeader.split(" ")[1];

      try {
        username = tokenManager.getUsernameFromToken(token);
      } catch (Exception e) {
        logger.error("an error occurred");
      }
    } else {
      logger.error("No authorization header provided");
    }

    if (username != null &&
        SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userService.loadUserByUsername(username);

      if (this.tokenManager.validateToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
      }
    }
  }
}
