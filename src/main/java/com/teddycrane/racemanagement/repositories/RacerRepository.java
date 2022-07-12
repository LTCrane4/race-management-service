package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.model.racer.Racer;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RacerRepository extends JpaRepository<Racer, UUID> {
  // TODO get the isDeleted query working
  @Query(
      nativeQuery = true,
      value =
          "SELECT * FROM racer R "
              + "WHERE (:first IS null OR R.first_name = CAST(:first as text)) "
              + "AND (:middle IS null OR R.middle_name = CAST(:middle as text) "
              + "AND (:last IS null OR R.last_name = CAST(:last as text) "
              + "AND (:team IS null OR R.team_name = CAST(:team as text) "
              + "AND (:category IS null OR R.category = CAST(:category as text)")
  Collection<Racer> queryRacers(
      @Param("first") String firstName,
      @Param("middle") String middleName,
      @Param("last") String lastName,
      @Param("team") String teamName,
      @Param("category") String category);
}
