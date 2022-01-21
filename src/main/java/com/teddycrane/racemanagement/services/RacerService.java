package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.Collection;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface RacerService {
  Collection<Racer> getAllRacers();

  Racer getRacer(UUID id) throws NotFoundException;
}
