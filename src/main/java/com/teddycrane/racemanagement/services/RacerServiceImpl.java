package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.SearchRacerRequest;
import com.teddycrane.racemanagement.repositories.RacerRepository;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    return this.racerRepository.findAll().stream()
        .filter(racer -> !racer.isDeleted())
        .collect(Collectors.toList());
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

    Instant now = Instant.now();
    Racer r =
        Racer.builder()
            .id(UUID.randomUUID())
            .createdTimestamp(now)
            .updatedTimestamp(now)
            .firstName(firstName)
            .lastName(lastName)
            .category(category)
            .middleName(middleName)
            .teamName(teamName)
            .phoneNumber(phoneNumber)
            .email(email)
            .bibNumber(bibNumber)
            .build();

    if (this.racerRepository.findById(r.getId()).isPresent()) {
      throw new DuplicateItemException(
          String.format("A racer with the id %s already exists", r.getId()));
    }

    return this.racerRepository.save(r);
  }

  @Override
  public Racer updateRacer(
      UUID id,
      @NonNull Instant updatedTimestamp,
      @Nullable String firstName,
      @Nullable String lastName,
      @Nullable String middleName,
      @Nullable String teamName,
      @Nullable String phoneNumber,
      @Nullable String email,
      @Nullable Integer bibNumber)
      throws ConflictException, NotFoundException {
    logger.info("updateRacer called for id {}", id);

    Racer r =
        this.racerRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("No racer found for the provided id"));

    // locking validation
    if (!updatedTimestamp.equals(r.getUpdatedTimestamp())) {
      throw new ConflictException(
          "The updated timestamp is out of date.  Please re-fetch and try again");
    }

    if (firstName != null) {
      r.setFirstName(firstName);
    }
    if (lastName != null) {
      r.setLastName(lastName);
    }
    if (middleName != null) {
      r.setMiddleName(middleName);
    }
    if (teamName != null) {
      r.setTeamName(teamName);
    }
    if (phoneNumber != null) {
      r.setPhoneNumber(phoneNumber);
    }
    // TODO email validation
    if (email != null) {
      r.setEmail(email);
    }
    if (bibNumber != null) {
      r.setBibNumber(bibNumber);
    }

    r.setUpdatedTimestamp(Instant.now());

    return this.racerRepository.save(r);
  }

  @Override
  public boolean deleteRacer(UUID id, @NonNull Instant updatedTimestamp)
      throws ConflictException, NotFoundException {
    logger.info("deleteRacer called for id {}", id);
    Racer r =
        this.racerRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("No racer found for the provided id"));

    if (!updatedTimestamp.equals(r.getUpdatedTimestamp())) {
      throw new ConflictException("The updated timestamp is not valid");
    }

    r.setDeleted(true);
    this.racerRepository.save(r);
    return true;
  }

  @Override
  public Collection<Racer> searchRacers(@NonNull @Validated SearchRacerRequest request) {
    // TODO remove the (new)
    logger.info("searchRacers (new) called");
    return this.racerRepository.queryRacers(
        request.getFirstName(),
        request.getMiddleName(),
        request.getLastName(),
        request.getTeamName(),
        request.getCategory().toString());
  }
}
