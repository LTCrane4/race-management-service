package com.teddycrane.racemanagement.enums;

public enum UserType {
  USER("user"),
  ADMIN("admin"),
  ROOT("root");

  private String text;

  private UserType(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return this.text;
  }

}
