package com.teddycrane.racemanagement.model.racer;

import com.teddycrane.racemanagement.enums.Category;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "racer")
public class Racer {

  @NonNull
  @Column(name = "created_timestamp", insertable = false, nullable = false, updatable = false)
  @Builder.Default
  private Instant createdTimestamp = Instant.now();

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "racer_id", unique = true, nullable = false, updatable = false, insertable = false)
  private UUID id;

  @Setter
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Setter
  @Column(name = "middle_name")
  private String middleName;

  @Setter
  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Setter
  @Column(name = "team_name")
  private String teamName;

  @Setter
  @Column(name = "phone_number")
  private String phoneNumber;

  @Setter
  @Email
  @Column(name = "email")
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private Category category;

  @Setter
  @NonNull
  @Column(name = "updated_timestamp", nullable = false)
  @Builder.Default
  private Instant updatedTimestamp = Instant.now();

  @Setter
  @Column(name = "bib_number")
  private int bibNumber;

  @Setter
  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;

  public Racer() {
    this.id = UUID.randomUUID();
    this.createdTimestamp = Instant.now();
    this.updatedTimestamp = Instant.now();
    this.isDeleted = false;
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

  @Deprecated
  public Racer(
      @NonNull String firstName,
      @NonNull String lastName,
      @NonNull Category category,
      String middleName,
      String teamName,
      String phoneNumber,
      String email,
      int bibNumber) {
    this();
    this.firstName = firstName;
    this.lastName = lastName;
    this.middleName = middleName;
    this.category = category;
    this.teamName = teamName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.bibNumber = bibNumber;
  }

  private Racer(@NonNull Racer other) {
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

  public static Racer copyOf(Racer original) {
    return new Racer(original);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        createdTimestamp,
        id,
        firstName,
        middleName,
        lastName,
        teamName,
        phoneNumber,
        email,
        category,
        updatedTimestamp,
        bibNumber,
        isDeleted);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Racer racer = (Racer) o;
    return bibNumber == racer.bibNumber
        && isDeleted == racer.isDeleted
        && createdTimestamp.equals(racer.createdTimestamp)
        && id.equals(racer.id)
        && Objects.equals(firstName, racer.firstName)
        && Objects.equals(middleName, racer.middleName)
        && Objects.equals(lastName, racer.lastName)
        && Objects.equals(teamName, racer.teamName)
        && Objects.equals(phoneNumber, racer.phoneNumber)
        && Objects.equals(email, racer.email)
        && category == racer.category
        && updatedTimestamp.equals(racer.updatedTimestamp);
  }
}
