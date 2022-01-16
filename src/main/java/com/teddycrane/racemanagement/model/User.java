package com.teddycrane.racemanagement.model;

import java.util.UUID;
import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

@Entity
public class User {
  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final UUID id;

  public User() { this.id = UUID.randomUUID(); }

  private User(UUID id) { this.id = id; }

  private boolean equals(User other) { return this.id.equals(other.id); }

  @Override
  public boolean equals(Object other) {
    if (other.getClass().equals(this.getClass())) {
      return this.equals((User)other);
    }
    return false;
  }
}
