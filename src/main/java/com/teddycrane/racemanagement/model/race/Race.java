package com.teddycrane.racemanagement.model.race;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.model.Response;
import com.teddycrane.racemanagement.model.racer.Racer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "race")
public class Race implements Response {

  @Id
  @Column(name = "race_id", updatable = false, nullable = false, insertable = false, unique = true)
  private final UUID id;

  @Column(name = "created_timestamp", updatable = false, nullable = false)
  @NotNull
  private final Instant createdTimestamp;

  @Column(name = "updated_timestamp")
  private Instant updatedTimestamp;

  @Column(name = "name")
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private Category category;

  @ManyToMany @Exclude Collection<Racer> racers;

  @Column(name = "event_date")
  private LocalDate eventDate;

  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(name = "finish_time")
  private LocalTime finishTime;

  private Race() {
    this.id = UUID.randomUUID();

    var instant = Instant.now();
    this.createdTimestamp = instant;
    this.updatedTimestamp = instant;
  }

  public Race(String name, Category category, Collection<Racer> racers) {
    this();
    this.name = name;
    this.category = category;
    this.racers = new ArrayList<>(racers);
  }

  public void addRacer(Racer newRacer) {
    if (!this.racers.contains(newRacer)) {
      this.racers.add(newRacer);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Race race = (Race) o;
    return id != null && Objects.equals(id, race.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
