package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RaceRepository;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RaceServiceImpl extends BaseService implements RaceService {

  private final RaceRepository raceRepository;
  private final RacerRepository racerRepository;

  public RaceServiceImpl(RaceRepository raceRepository, RacerRepository racerRepository) {
    super();
    this.raceRepository = raceRepository;
    this.racerRepository = racerRepository;
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
    logger.info("createRace called with name {} and category {}", name, category);

    this.raceRepository
        .findByName(name)
        .ifPresent(
            (race) -> {
              if (race.getCategory() == category) {
                throw new ConflictException("Name collision!");
              }
            });

    // resolve racers
    Collection<Racer> racers = this.racerRepository.findAllById(racerIds);
    Race r = new Race(name, category, racers);

    return this.raceRepository.save(r);
  }
}
