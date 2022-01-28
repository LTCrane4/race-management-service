package com.teddycrane.racemanagement.security.filter;

import com.teddycrane.racemanagement.security.util.TokenManager;
import com.teddycrane.racemanagement.services.AuthenticationService;
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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private final Logger logger;
  private final AuthenticationService userService;

  private final TokenManager tokenManager;

  public AuthenticationFilter(AuthenticationService userService, TokenManager tokenManager) {
    this.logger = LogManager.getLogger(this.getClass());
    this.userService = userService;
    this.tokenManager = tokenManager;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    logger.debug("doFilterInternal called");
    String tokenHeader = request.getHeader("Authorization");
    String username = null;
    String token = null;

    if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
      token = tokenHeader.split(" ")[1];

      try {
        username = tokenManager.getUsernameFromToken(token);
      } catch (Exception e) {
        logger.error("No username was found in the provided token!");
      }
    } else {
      logger.debug("No authorization header provided");
    }

    if (username != null) {
      logger.debug("Validating token data");
      UserDetails userDetails = this.userService.loadUserByUsername(username);

      if (this.tokenManager.validateToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
