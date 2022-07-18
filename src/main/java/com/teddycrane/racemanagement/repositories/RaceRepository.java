package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.model.race.Race;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Query(
      nativeQuery = true,
      value =
          "SELECT * FROM race R "
              + "WHERE (:name IS null OR R.name = CAST(:name as text)) "
              + "AND (:category IS null OR R.category = CAST(:category as text))")
  Collection<Race> queryRaces(@Param("name") String name, @Param("category") String category);
}
