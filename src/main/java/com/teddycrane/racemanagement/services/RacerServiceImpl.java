package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import java.util.Collection;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RacerServiceImpl extends BaseService implements RacerService {

  private final RacerRepository racerRepository;

  public RacerServiceImpl(RacerRepository racerRepository) {
    super();
    this.racerRepository = racerRepository;
  }

  @Override
  public Collection<Racer> getAllRacers() {
    logger.info("getAllRacers called");

    return this.racerRepository.findAll();
  }

  @Override
  public Racer getRacer(UUID id) throws NotFoundException {
    logger.info("getRacer called for id {}", id);

    return this.racerRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("No user found for the provided id"));
  }

  @Override
  public Racer createRacer(
      String firstName,
      String lastName,
      Category category,
      String middleName,
      String teamName,
      String phoneNumber,
      String email,
      int bibNumber)
      throws DuplicateItemException {
    logger.info("createRacer called");

    return null;
  }
}
