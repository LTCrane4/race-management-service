package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.model.race.Race;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, UUID> {
  Optional<Race> findByName(String name);
}
