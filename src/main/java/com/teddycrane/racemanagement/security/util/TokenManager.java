package com.teddycrane.racemanagement.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenManager implements Serializable {

  private static final long serialVersionUID = 7008375124389347049L;

  @Value("${secret}")
  private String jwtSecret;

  private static final long TOKEN_VALIDITY = 30 * (10 * 60 * 60);

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setIssuer("com.teddycrane.racemanagement")
        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY + 1000))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    String username = this.getUsernameFromToken(token);
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    boolean isTokenExpired = claims.getExpiration().before(new Date());
    return (username.equals(userDetails.getUsername())) && !isTokenExpired;
  }

  public String getUsernameFromToken(String token) {
    final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }
}
