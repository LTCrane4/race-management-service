package com.teddycrane.racemanagement.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.teddycrane.racemanagement.model.user.User;

public class FieldExclusionStrategy implements ExclusionStrategy {
  public boolean shouldSkipClass(Class<?> arg0) { return false; }

  /**
   * Excludes fields from the specified class matching the specified name.
   */
  public boolean shouldSkipField(FieldAttributes f) {
    return (f.getDeclaringClass() == User.class &&
            f.getName().equals("password"));
  }
}
