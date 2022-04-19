package com.teddycrane.racemanagement.security.util;

import com.teddycrane.racemanagement.model.user.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenManager implements Serializable {

  private static final long serialVersionUID = 7008375124389347049L;
  private static final String ISSUER = "com.teddycrane.racemanagement";

  @Value("${secret}")
  private String jwtSecret;

  private static final long TOKEN_VALIDITY = 60 * 60;

  public TokenManager() {}

  public TokenManager(@Value("${secret}") String secret) {
    this.jwtSecret = secret;
  }

  public String generateToken(UserDetails userDetails) {
    UserPrincipal details = (UserPrincipal) userDetails;

    Map<String, Object> claims = new HashMap<>();
    claims.put("iat", Instant.now().toEpochMilli() / 1000);
    claims.put("exp", (Instant.now().toEpochMilli() / 1000) + TOKEN_VALIDITY + 1000);
    claims.put("roles", details.getUser().getUserType());
    claims.put("user", details.getUsername());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(details.getUser().getId().toString())
        .setIssuer(ISSUER)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    UserPrincipal details = (UserPrincipal) userDetails;

    String username = this.getUsernameFromToken(token);
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    boolean isTokenExpired = claims.getExpiration().before(new Date());

    boolean doClaimsMatch =
        claims.getIssuer().equals(ISSUER)
            && username.equals(details.getUsername())
            && ((String) claims.get("roles"))
                .equalsIgnoreCase(details.getUser().getUserType().toString())
            && UUID.fromString(claims.getSubject()).equals(details.getUser().getId());

    return doClaimsMatch && !isTokenExpired;
  }

  public String getUsernameFromToken(String token) {
    final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return (String) claims.get("user");
  }
}
