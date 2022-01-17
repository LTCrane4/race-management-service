package com.teddycrane.racemanagement.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class BaseService {
  protected final Logger logger = LogManager.getLogger(this.getClass());
}
