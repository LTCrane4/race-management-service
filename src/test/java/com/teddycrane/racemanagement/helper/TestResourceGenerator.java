package com.teddycrane.racemanagement.helper;

import com.github.javafaker.Faker;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.User;
import java.util.ArrayList;

public class TestResourceGenerator {
  private static Faker faker = new Faker();

  public static User generateUser() {
    return new User(faker.name().firstName(), faker.name().lastName(),
                    faker.name().username(), faker.bothify("????##@fake.fake"),
                    faker.bothify("??????"), UserType.USER);
  }

  public static Iterable<User> generateUserList(int length) {
    ArrayList<User> result = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      result.add(generateUser());
    }

    return result;
  }
}
