package com.teddycrane.racemanagement.helper;

import com.github.javafaker.Faker;
import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.user.User;
import java.util.ArrayList;
import java.util.Collection;

public class TestResourceGenerator {

  private TestResourceGenerator() {}

  private static Faker faker = new Faker();

  public static User generateUser() {
    return new User(
        faker.name().firstName(), faker.name().lastName(),
        faker.name().username(), faker.bothify("????##@fake.fake"),
        faker.bothify("??????"), UserType.USER);
  }

  public static Collection<User> generateUserList(int length) {
    ArrayList<User> result = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      result.add(generateUser());
    }

    return result;
  }

  public static Racer generateRacer() {
    return new Racer(
        faker.name().firstName(), faker.name().lastName(), Category.CAT1, faker.name().firstName());
  }

  public static Collection<Racer> generateRacerList(int length) {
    ArrayList<Racer> result = new ArrayList<>();

    for (int i = 0; i < length; i++) {
      result.add(generateRacer());
    }
    return result;
  }
}
