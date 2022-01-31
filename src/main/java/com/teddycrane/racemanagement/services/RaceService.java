package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.race.Race;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface RaceService {
  List<Race> getAllRaces();

  Race getRace(UUID id);
}
