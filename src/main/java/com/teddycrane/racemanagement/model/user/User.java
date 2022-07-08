package com.teddycrane.racemanagement.model.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddycrane.racemanagement.enums.UserStatus;
import com.teddycrane.racemanagement.enums.UserType;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "_user")
public class User {

  @Id
  @Column(name = "user_id", nullable = false, updatable = false, unique = true)
  private final UUID id;

  @Column(name = "created_timestamp", nullable = false, updatable = false)
  private final Instant createdTimestamp;

  @Setter
  @NonNull
  @Column(name = "first_name")
  private String firstName;

  @Setter
  @NonNull
  @Column(name = "last_name")
  private String lastName;

  @Setter
  @Column(name = "email")
  @Email
  @NonNull
  private String email;

  @Setter
  @NonNull
  @Column(name = "username")
  private String username;

  @Setter
  @NonNull
  @Column(name = "password")
  private String password;

  @Setter
  @Enumerated(EnumType.STRING)
  @NonNull
  @Column(name = "user_type")
  private UserType userType;

  @Enumerated(EnumType.STRING)
  @Setter
  @NonNull
  @Column(name = "status")
  private UserStatus status;

  @NonNull
  @Column(name = "updated_timestamp")
  @Setter
  private Instant updatedTimestamp;

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
      @NonNull String firstName,
      @NonNull String lastName,
      @NonNull String username,
      @NonNull String email,
      @NonNull String password,
      @NonNull UserType userType,
      @NonNull UserStatus status,
      Instant createdTimestamp,
      @NonNull Instant updatedTimestamp) {
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

  @Generated
  public User(
      UUID id,
      String firstName,
      String lastName,
      String username,
      String email,
      String password,
      UserType userType,
      @NonNull UserStatus status) {
    this(id, firstName, lastName, username, email, password, userType);
    this.status = status;
  }

  public User(
      UUID id,
      @NonNull String firstName,
      @NonNull String lastName,
      @NonNull String username,
      @NonNull String email,
      @NonNull String password,
      @NonNull UserType userType) {
    this(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
    this.userType = userType;
  }

  public User(
      @NonNull String firstName,
      @NonNull String lastName,
      @NonNull String username,
      @NonNull String email,
      @NonNull String password) {
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
      @NonNull UserType userType) {
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
      @NonNull UserStatus status) {
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

  public void setUpdatedTimestamp() {
    this.updatedTimestamp = Instant.now();
  }

  @Override
  public String toString() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "";
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    if (!id.equals(user.id)) {
      return false;
    }
    if (!createdTimestamp.equals(user.createdTimestamp)) {
      return false;
    }
    if (!firstName.equals(user.firstName)) {
      return false;
    }
    if (!lastName.equals(user.lastName)) {
      return false;
    }
    if (!email.equals(user.email)) {
      return false;
    }
    if (!username.equals(user.username)) {
      return false;
    }
    if (!password.equals(user.password)) {
      return false;
    }
    if (userType != user.userType) {
      return false;
    }
    if (status != user.status) {
      return false;
    }
    return updatedTimestamp.equals(user.updatedTimestamp);
  }
}
