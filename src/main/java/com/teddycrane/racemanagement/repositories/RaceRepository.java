package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.model.race.Race;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, UUID> {
  Optional<Race> findByName(String name);

  @Query(
      nativeQuery = true,
      value =
          "SELECT race.id FROM race INNER JOIN race_racers ON race_racers.racers_id='?1'"
              + " AND race.id=race_racers.race_id")
  List<String> findRacesByRacerId(String racerId);
}
