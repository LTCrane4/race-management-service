package com.teddycrane.racemanagement.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

  private PasswordEncoder() {}

  /**
   * Encodes password with BCrypt.
   *
   * @param input The password to be encoded. Raw String.
   * @return The encoded password.
   */
  public static String encodePassword(String input) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(input);
  }
}
