package com.teddycrane.racemanagement.helper;

import com.github.javafaker.Faker;
import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.enums.UserType;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.user.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class TestResourceGenerator {

  private static final Faker faker = new Faker();

  private TestResourceGenerator() {}

  public static User generateUser() {
    return new User(
        faker.name().firstName(), faker.name().lastName(),
        faker.name().username(), faker.bothify("????##@fake.fake"),
        faker.bothify("??????"), UserType.USER);
  }

  public static User generateUser(UserType type) {
    return new User(
        faker.name().firstName(),
        faker.name().lastName(),
        faker.name().username(),
        faker.bothify("????##@fake.fake"),
        faker.bothify("??????"),
        type);
  }

  public static Collection<User> generateUserList(int length) {
    ArrayList<User> result = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      result.add(generateUser());
    }

    return result;
  }

  public static Racer generateRacer(Category category) {
    return new Racer(
        faker.name().firstName(), faker.name().lastName(), category, faker.name().firstName());
  }

  public static Racer generateRacer() {
    return generateRacer(Category.CAT1);
  }

  public static Collection<Racer> generateRacerList(int length, Category category) {
    ArrayList<Racer> result = new ArrayList<>();

    for (int i = 0; i < length; i++) {
      result.add(generateRacer(category));
    }
    return result;
  }

  public static Collection<Racer> generateRacerList(int length) {
    return generateRacerList(length, Category.CAT2);
  }

  public static Race generateRace(Category category) {
    var race =
        Race.builder()
            .id(UUID.randomUUID())
            .category(category)
            .name(faker.name().name())
            .racers(generateRacerList(5, category))
            .build();
    return race;
  }

  public static Race generateRace() {
    return generateRace(Category.CAT2);
  }

  public static Collection<Race> generateRaceList() {
    Collection<Race> races = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      races.add(generateRace());
    }
    return races;
  }
}
