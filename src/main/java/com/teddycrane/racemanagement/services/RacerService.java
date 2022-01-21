package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.Collection;
import org.springframework.stereotype.Service;

@Service
public interface RacerService {
  Collection<Racer> getAllRacers();
}
