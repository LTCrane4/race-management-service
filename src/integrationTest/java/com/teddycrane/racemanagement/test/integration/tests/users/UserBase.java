package com.teddycrane.racemanagement.test.integration.tests.users;

import com.teddycrane.racemanagement.test.integration.tests.IntegrationBase;

abstract class UserBase extends IntegrationBase {
  private static final String BASE_USER_PATH = "/user";
  protected static final String GET_USER_PATH = BASE_USER_PATH + "/";
  protected static final String CREATE_USER_PATH = BASE_USER_PATH + "/new";

  protected static final String USER_ID = "bf68eea7-25a6-4dbf-867b-0829e308bbdd";
  protected static final String USERNAME_PATH = "$.username";
  protected static final String PASSWORD_PATH = "$.password";
  protected static final String FIRST_NAME_PATH = "$.firstName";
}
