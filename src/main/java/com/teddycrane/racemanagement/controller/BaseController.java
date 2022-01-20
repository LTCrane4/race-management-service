package com.teddycrane.racemanagement.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

/** A parent class for all controllers in the project, to allow reuse of logger boilerplate. */
@RestController
public abstract class BaseController {
  protected final Logger logger = LogManager.getLogger(this.getClass());
}
