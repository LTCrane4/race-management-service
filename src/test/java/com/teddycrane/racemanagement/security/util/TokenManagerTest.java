package com.teddycrane.racemanagement.security.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.binary.Base64;
import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.UserPrincipal;
import io.jsonwebtoken.Jwts;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenManagerTest {

  private static final String ISSUER = "com.teddycrane.racemanagement";
  private TokenManager tokenManager;

  private String testSecretValue;

  private String randomBase64() {
    Random r = new Random();
    byte[] bytes = new byte[256];
    r.nextBytes(bytes);
    return Base64.encodeBase64String(bytes);
  }

  @BeforeEach
  void setUp() {
    testSecretValue = this.randomBase64();
    this.tokenManager = new TokenManager(testSecretValue);
  }

  @Test
  @DisplayName("The token manager should generate and successfully validate valid tokens")
  void tokenManagerShouldGenerateValidTokens() {
    var user = TestResourceGenerator.generateUser();
    var userDetails = new UserPrincipal(user);
    var token = tokenManager.generateToken(userDetails);

    var parsedClaims = Jwts.parser().setSigningKey(testSecretValue).parseClaimsJws(token).getBody();

    assertAll(
        () -> assertNotNull(token, "The token generated should not be null"),
        () ->
            assertNotNull(
                parsedClaims, "The claims should be parsable and not return a null value"),
        () ->
            assertEquals(
                parsedClaims.getSubject(),
                user.getId().toString(),
                "The token subject should equal the user ID"),
        () ->
            assertEquals(
                parsedClaims.getIssuer(),
                ISSUER,
                "The issuer should match the application groupname"),
        () ->
            assertEquals(
                ((String) parsedClaims.get("roles")).toLowerCase(),
                user.getUserType().toString(),
                "The token roles should match the user type"),
        () ->
            assertEquals(
                (String) parsedClaims.get("user"),
                user.getUsername(),
                "The user claim should match the username"));

    assertTrue(
        tokenManager.validateToken(token, userDetails),
        "The token manager should successfully validate a token");
  }
}
