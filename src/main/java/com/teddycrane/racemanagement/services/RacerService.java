package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
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
      Date updatedTimestamp,
      String firstName,
      String lastName,
      String middleName,
      String teamName,
      String phoneNumber,
      String email)
      throws NotFoundException;
}
