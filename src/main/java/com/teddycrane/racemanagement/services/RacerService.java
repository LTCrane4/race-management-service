package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public interface RacerService {

  Collection<Racer> getAllRacers();

  Racer getRacer(UUID id) throws NotFoundException;

  Racer createRacer(
      String firstName,
      String lastName,
      Category category,
      String middleName,
      String teamName,
      String phoneNumber,
      String email,
      int bibNumber)
      throws DuplicateItemException;

  Racer updateRacer(
      UUID id,
      Instant updatedTimestamp,
      @Nullable String firstName,
      @Nullable String lastName,
      @Nullable String middleName,
      @Nullable String teamName,
      @Nullable String phoneNumber,
      @Nullable String email)
      throws ConflictException, NotFoundException;

  boolean deleteRacer(UUID id, Instant updatedTimestamp)
      throws ConflictException, NotFoundException;
}
