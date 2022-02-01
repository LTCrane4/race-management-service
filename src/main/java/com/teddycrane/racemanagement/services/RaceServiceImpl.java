package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.repositories.RaceRepository;
import java.util.List;
import java.util.UUID;
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

  @Override
  public Race getRace(UUID id) {
    logger.info("getRace called with id {}", id);

    return this.raceRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("No user found for the provided id"));
  }

  @Override
  public Race createRace(String name, Category category, List<UUID> racerIds)
      throws ConflictException {
    return null;
  }
}
