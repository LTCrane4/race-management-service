package com.teddycrane.racemanagement.enums;

public enum UserStatus {
  ACTIVE("active"),
  DISABLED("disabled"),
  TERMINATED("terminated");

  private final String text;

  private UserStatus(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return this.text;
  }
}
