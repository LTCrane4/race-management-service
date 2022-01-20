package com.teddycrane.racemanagement.enums;

public enum UserType {
  USER("user"),
  ADMIN("admin"),
  ROOT("root");

  private final String text;

  private UserType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return this.text;
  }
}
