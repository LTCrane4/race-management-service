package com.teddycrane.racemanagement.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UtilityClass
public class PasswordEncoder {

  private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  /**
   * Encodes password with BCrypt.
   *
   * @param input The password to be encoded. Raw String.
   * @return The encoded password.
   */
  public static String encodePassword(String input) {
    return encoder.encode(input);
  }

  public static boolean validatePassword(String oldPassword, String newPassword) {
    return encoder.matches(newPassword, oldPassword);
  }
}
