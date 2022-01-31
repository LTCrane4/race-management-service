package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.repositories.RaceRepository;
import java.util.List;

public class RaceServiceImpl extends BaseService implements RaceService {

  private RaceRepository raceRepository;

  public RaceServiceImpl(RaceRepository raceRepository) {
    super();
    this.raceRepository = raceRepository;
  }

  @Override
  public List<Race> getAllRaces() {
    return null;
  }
}
