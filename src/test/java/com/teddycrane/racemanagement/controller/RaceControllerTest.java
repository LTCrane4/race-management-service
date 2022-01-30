package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.services.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RaceControllerTest {

  @Mock private RaceService raceService;

  private RaceApi raceController;

  @BeforeEach
  void setUp() {}
}
