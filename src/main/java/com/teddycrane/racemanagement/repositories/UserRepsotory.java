package com.teddycrane.racemanagement.repositories;

import com.teddycrane.racemanagement.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepsotory extends JpaRepository<User, UUID> {}
