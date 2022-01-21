package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import java.util.Collection;
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
}
