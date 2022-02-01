package com.teddycrane.racemanagement.model.racer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddycrane.racemanagement.enums.Category;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;

@Entity
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class Racer {

  @NonNull private final Instant createdTimestamp;

  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Setter @NonNull private String firstName;
  @Setter private String middleName;
  @Setter @NonNull private String lastName;
  @Setter private String teamName, phoneNumber;
  @Setter @Email private String email;

  @Enumerated(EnumType.STRING)
  @NonNull
  private Category category;

  @Setter @NonNull private Instant updatedTimestamp;

  @Setter private int bibNumber;

  @Setter @Builder.Default private boolean isDeleted = false;

  public Racer() {
    this.id = UUID.randomUUID();

    Instant now = Instant.now();
    this.createdTimestamp = now;
    this.updatedTimestamp = now;
  }

  private Racer(
      @NonNull String firstName,
      @NonNull String lastName,
      @NonNull Category category,
      String middleName,
      String teamName,
      String phoneNumber,
      String email,
      @NonNull Instant createdTimestamp,
      @NonNull Instant updatedTimestamp,
      int bibNumber,
      boolean isDeleted,
      UUID id) {
    this.id = id;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.category = category;
    this.teamName = teamName;
    this.phoneNumber = phoneNumber;
    // todo set up email validation
    this.email = email;
    this.createdTimestamp = createdTimestamp;
    this.updatedTimestamp = updatedTimestamp;
    this.bibNumber = bibNumber;
    this.isDeleted = isDeleted;
  }

  public Racer(
      @NonNull String firstName,
      @NonNull String lastName,
      @NonNull Category category,
      String middleName) {
    this();
    this.firstName = firstName;
    this.lastName = lastName;
    this.category = category;
    this.middleName = middleName;
  }

  public Racer(
      @NonNull String firstName,
      @NonNull String lastName,
      @NonNull Category category,
      String middleName,
      String teamName,
      String phoneNumber,
      String email,
      int bibNumber) {
    this(firstName, lastName, category, middleName);
    this.teamName = teamName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.bibNumber = bibNumber;
  }

  public Racer(@NonNull Racer other) {
    this(
        other.firstName,
        other.lastName,
        other.category,
        other.middleName,
        other.teamName,
        other.phoneNumber,
        other.email,
        other.createdTimestamp,
        other.updatedTimestamp,
        other.bibNumber,
        other.isDeleted,
        other.id);
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
}
