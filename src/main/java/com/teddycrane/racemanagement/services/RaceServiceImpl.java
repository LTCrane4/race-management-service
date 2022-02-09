package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RaceRepository;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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

  @Override
  public Race addRacersToRace(UUID raceId, List<UUID> racerIds, Instant updatedTimestamp)
      throws ConflictException, NotFoundException {
    logger.info("addRacersToRace called for race id {}", raceId);

    Race r =
        this.raceRepository
            .findById(raceId)
            .orElseThrow(() -> new NotFoundException(raceId.toString()));

    if (!r.getUpdatedTimestamp().equals(updatedTimestamp)) {
      throw new ConflictException("Newer data exists.  Re-fetch and try again.");
    }

    // filter out racers that do not have the correct category
    Collection<Racer> newRacers =
        this.racerRepository.findAllById(racerIds).stream()
            .filter((racer) -> r.getCategory().equals(racer.getCategory()))
            .collect(Collectors.toList());

    Collection<Racer> racers = r.getRacers();
    racers.addAll(newRacers);
    r.setRacers(racers);

    // update audit timestamp
    r.setUpdatedTimestamp(Instant.now());

    return this.raceRepository.save(r);
  }

  @Override
  public Race updateRace(UUID id, String name, Category category, Instant updatedTimestamp)
      throws ConflictException, NotFoundException {
    logger.info("updateRace called for race {}", id);
    boolean isUpdated = false;

    Race race =
        this.raceRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("No racer found!"));

    if (!updatedTimestamp.equals(race.getUpdatedTimestamp())) {
      throw new ConflictException("Updated timestamps do not match!");
    }

    if (name != null) {
      race.setName(name);
      isUpdated = true;
    }
    // todo ensure that racer list is empty
    if (category != null) {
      race.setCategory(category);
      isUpdated = true;
    }

    if (isUpdated) {
      race.setUpdatedTimestamp(Instant.now());
    }

    return this.raceRepository.save(race);
  }

  @Override
  public List<Race> getRacesForRacer(UUID racerId) throws NotFoundException {
    logger.info("getRacesForRacer called for racer {}", racerId);
    try {
      this.racerRepository.existsById(racerId);
    } catch (IllegalArgumentException e) {
      throw new NotFoundException("No racer found for the provided id");
    }

    List<Race> races =
        this.raceRepository.findAll().stream()
            .filter(
                race ->
                    race.getRacers().stream()
                        .map(
                            racer -> {
                              return racer.getId();
                            })
                        .collect(Collectors.toList())
                        .contains(racerId))
            .collect(Collectors.toList());

    return races;
  }

  @Override
  public Race startRace(UUID raceId, LocalDate startDate, LocalTime startTime)
      throws ConflictException, NotFoundException {
    return null;
  }
}
