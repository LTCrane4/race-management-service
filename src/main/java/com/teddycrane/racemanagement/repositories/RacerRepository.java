package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RacerRepository extends JpaRepository<Racer, UUID> {}
