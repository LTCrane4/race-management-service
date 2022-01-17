package com.teddycrane.racemanagement.enums;

public enum UserStatus {
  ACTIVE("active"),
  DISABLED("disabled"),
  LOCKED("locked"),
  INACTIVE("inactive");

  private final String text;

  UserStatus(final String text) { this.text = text; }

  @Override
  public String toString() {
    return this.text;
  }
}
