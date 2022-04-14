package com.teddycrane.racemanagement.model.user;

import com.teddycrane.racemanagement.enums.UserStatus;
import com.teddycrane.racemanagement.enums.UserType;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Entity
@Getter
@EqualsAndHashCode
@ToString
@Table(name = "_user")
public class User {

  @Id private final UUID id;

  private final Instant createdTimestamp;

  @Setter @NonNull private String firstName, lastName, email, username, password;

  @Setter
  @Enumerated(EnumType.STRING)
  @NonNull
  private UserType userType;

  @Enumerated(EnumType.STRING)
  @Setter
  @NonNull
  private UserStatus status;

  @Setter @NonNull private Instant updatedTimestamp;

  public User() {
    this(UUID.randomUUID());
  }

  private User(UUID id) {
    this.id = id;

    var instant = Instant.now();
    this.createdTimestamp = instant;
    this.updatedTimestamp = instant;
  }

  private User(
      UUID id,
      String firstName,
      String lastName,
      String username,
      String email,
      String password,
      UserType userType,
      UserStatus status,
      Instant createdTimestamp,
      Instant updatedTimestamp) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.userType = userType;
    this.status = status;
    this.createdTimestamp = createdTimestamp;
    this.updatedTimestamp = updatedTimestamp;
  }

  public User(
      UUID id,
      String firstName,
      String lastName,
      String username,
      String email,
      String password,
      UserType userType) {
    this(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
    this.userType = userType;
  }

  public User(String firstName, String lastName, String username, String email, String password) {
    this();
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email; // TODO update email validation
    this.username = username;
    this.password = password;
  }

  public User(
      String firstName,
      String lastName,
      String username,
      String email,
      String password,
      UserType userType) {
    this(firstName, lastName, username, email, password);
    this.userType = userType;
  }

  public User(
      String firstName,
      String lastName,
      String username,
      String email,
      String password,
      UserType userType,
      UserStatus status) {
    this(firstName, lastName, username, email, password, userType);
    this.status = status;
  }

  public User(@NonNull User other) {
    this(
        other.id,
        other.firstName,
        other.lastName,
        other.username,
        other.email,
        other.password,
        other.userType,
        other.status,
        other.createdTimestamp,
        other.updatedTimestamp);
  }
}
