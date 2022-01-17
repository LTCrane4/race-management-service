package com.teddycrane.racemanagement.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teddycrane.racemanagement.config.FieldExclusionStrategy;
import com.teddycrane.racemanagement.enums.UserType;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;

@Entity
@Getter
public class User {
  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final UUID id;

  @Setter
  @NonNull
  private String firstName, lastName, email, username, password;

  @Setter @Enumerated(EnumType.STRING) @NonNull private UserType userType;

  public User() { this.id = UUID.randomUUID(); }

  private User(UUID id) { this.id = id; }

  private User(UUID id, String firstName, String lastName, String username,
               String email, String password, UserType userType) {
    this(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
    this.userType = userType;
  }

  public User(String firstName, String lastName, String username, String email,
              String password) {
    this();
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email; // TODO update email validation
    this.username = username;
    this.password = password;
  }

  public User(String firstName, String lastName, String username, String email,
              String password, UserType userType) {
    this(firstName, lastName, username, email, password);
    this.userType = userType;
  }

  // TODO enable when user status works
  //  public User(String firstName, String lastName, String username, String
  //  email,
  //              String password, UserType userType, UserStatus status) {
  //    this(firstName, lastName, username, email, password, userType);
  //    this.status = status;
  //  }

  public User(User other) {
    this(other.id, other.firstName, other.lastName, other.username, other.email,
         other.password, other.userType);
  }

  private boolean equals(User other) {
    return this.id.equals(other.id) && this.firstName.equals(other.firstName) &&
        this.lastName.equals(other.lastName) &&
        this.username.equals(other.username) &&
        this.password.equals(other.password) &&
        this.email.equals(other.email) && this.userType.equals(other.userType);
  }

  @Override
  public boolean equals(Object other) {
    if (other.getClass().equals(this.getClass())) {
      return this.equals((User)other);
    }
    return false;
  }

  @Override
  public String toString() {
    Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new FieldExclusionStrategy())
                    .create();
    return gson.toJson(this);
  }
}
