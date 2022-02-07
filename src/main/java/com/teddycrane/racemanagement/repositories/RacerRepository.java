package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.enums.Category;
import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacerRepository extends JpaRepository<Racer, UUID> {
  Optional<Racer> findByFirstNameAndLastName(String firstName, String lastName);

  List<Racer> findAllByCategory(Category c);

  List<Racer> findAllByFirstName(String firstName);

  List<Racer> findAllByLastName(String lastName);
}
