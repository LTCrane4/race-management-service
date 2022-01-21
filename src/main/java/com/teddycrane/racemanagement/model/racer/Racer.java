package com.teddycrane.racemanagement.model.racer;

import com.google.gson.Gson;
import com.teddycrane.racemanagement.enums.Category;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;

@Entity
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Racer {

  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Setter @NonNull private String firstName;

  @Setter private String middleName;

  @Setter @NonNull private String lastName;

  @Setter private String teamName, phoneNumber, email;

  @Enumerated(EnumType.STRING)
  @NonNull
  private Category category;

  @Setter private int bibNumber;

  public Racer() {
    this.id = UUID.randomUUID();
  }

  private Racer(
      @NonNull String firstName,
      @NonNull String lastName,
      @NonNull Category category,
      String middleName,
      String teamName,
      String phoneNumber,
      String email,
      int bibNumber,
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
    this.bibNumber = bibNumber;
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

  public Racer(Racer other) {
    this(
        other.firstName,
        other.lastName,
        other.category,
        other.middleName,
        other.teamName,
        other.phoneNumber,
        other.email,
        other.bibNumber,
        other.id);
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
