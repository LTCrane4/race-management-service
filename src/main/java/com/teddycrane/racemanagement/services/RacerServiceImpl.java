package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import java.util.Collection;
import java.util.Date;
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

    Racer r =
        new Racer(
            firstName, lastName, category, middleName, teamName, phoneNumber, email, bibNumber);

    return this.racerRepository.save(r);
  }

  @Override
  public Racer updateRacer(
      UUID id,
      Date updatedTimestamp,
      String firstName,
      String lastName,
      String middleName,
      String teamName,
      String phoneNumber,
      String email)
      throws ConflictException, NotFoundException {
    logger.info("updateRacer called");

    Racer r =
        this.racerRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("No racer found for the provided id"));

    // locking validation
    if (!updatedTimestamp.equals(r.getUpdatedTimestamp())) {
      throw new ConflictException(
          "The updated timestamp is out of date.  Please re-fetch and try again");
    }

    if (firstName != null) r.setFirstName(firstName);
    if (lastName != null) r.setLastName(lastName);
    if (middleName != null) r.setMiddleName(middleName);
    if (teamName != null) r.setTeamName(teamName);
    if (phoneNumber != null) r.setPhoneNumber(phoneNumber);
    // todo email validation
    if (email != null) r.setEmail(email);

    r.setUpdatedTimestamp(new Date(System.currentTimeMillis()));

    return this.racerRepository.save(r);
  }
}
