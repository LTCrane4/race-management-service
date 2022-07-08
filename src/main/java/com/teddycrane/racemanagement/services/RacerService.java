package com.teddycrane.racemanagement.services;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.error.ConflictException;
import com.teddycrane.racemanagement.error.DuplicateItemException;
import com.teddycrane.racemanagement.error.NotFoundException;
import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.request.SearchRacerRequest;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public interface RacerService {

  /**
   * Finds all active racers.
   *
   * @return A {@code Collection<Racer>} containing all available racers.
   */
  Collection<Racer> getAllRacers();

  /**
   * Gets a single {@code Racer}.
   *
   * @param id The UUID to find the {@code Racer} by.
   * @return The found {@code Racer}.
   * @throws NotFoundException Thrown if no racer is found for the provided id.
   */
  Racer getRacer(UUID id) throws NotFoundException;

  /**
   * Creates a new {@link com.teddycrane.racemanagement.model.racer.Racer}.
   *
   * @param firstName The first name for the racer.
   * @param lastName The last name for the racer.
   * @param category The desired {@link com.teddycrane.racemanagement.enums.Category} for the racer.
   * @param middleName The middle name for the racer. Can be null.
   * @param teamName The team name for the racer. Can be null.
   * @param phoneNumber The phone number for the racer. Can be null.
   * @param email The email for the racer.
   * @param bibNumber The bib number for the racer.
   * @return The created {@link com.teddycrane.racemanagement.model.racer.Racer}.
   * @throws DuplicateItemException Thrown if a {@link
   *     com.teddycrane.racemanagement.model.racer.Racer} already exists in the database.
   */
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

  /**
   * Updates a {@link com.teddycrane.racemanagement.model.racer.Racer} with new values. If all
   * values are null, this is a no-op.
   *
   * @param id The UUID of the Racer to update. Required.
   * @param updatedTimestamp The {@code updatedTimestamp} of the Racer with the provided UUID. Used
   *     to ensure that the client is comparing to the most recent data.
   * @param firstName The new first name.
   * @param lastName The new last name.
   * @param middleName The new middle name.
   * @param teamName The new team name.
   * @param phoneNumber The new phone number.
   * @param email The new email address.
   * @param bibNumber The new bib number.
   * @return The updated {@link com.teddycrane.racemanagement.model.racer.Racer}.
   * @throws ConflictException Thrown if the {@code updatedTimestamp} does not match the value in
   *     the database.
   * @throws NotFoundException Thrown if no racer is found for the provided id.
   */
  Racer updateRacer(
      UUID id,
      Instant updatedTimestamp,
      @Nullable String firstName,
      @Nullable String lastName,
      @Nullable String middleName,
      @Nullable String teamName,
      @Nullable String phoneNumber,
      @Nullable String email,
      @Nullable Integer bibNumber)
      throws ConflictException, NotFoundException;

  boolean deleteRacer(UUID id, Instant updatedTimestamp)
      throws ConflictException, NotFoundException;

  Collection<Racer> searchRacers(SearchRacerRequest request);
}
