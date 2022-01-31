package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.repositories.RaceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RaceServiceImpl extends BaseService implements RaceService {

  private final RaceRepository raceRepository;

  public RaceServiceImpl(RaceRepository raceRepository) {
    super();
    this.raceRepository = raceRepository;
  }

  @Override
  public List<Race> getAllRaces() {
    logger.info("getAllRaces called");

    return this.raceRepository.findAll();
  }
}
