package com.teddycrane.racemanagement.test.integration.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtTokenProviderMock {

  // allow this secret since it's not actually a secret
  private static final String MOCK_JWT_SECRET =
      "secret-key-for-test-purposes-only"; // pragma: allowlist-secret

  public static String generateMockToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000))
        .signWith(SignatureAlgorithm.HS512, MOCK_JWT_SECRET)
        .compact();
  }
}
