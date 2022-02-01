package com.teddycrane.racemanagement.model.race;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Race {
  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final UUID id;

  private String name;

  @Enumerated(EnumType.STRING)
  private Category category;

  @OneToMany private Collection<Racer> racers;

  private Race() {
    this.id = UUID.randomUUID();
  }

  public Race(String name, Category category, Collection<Racer> racers) {
    this();
    this.name = name;
    this.category = category;
    this.racers = new ArrayList<>(racers);
  }
}
