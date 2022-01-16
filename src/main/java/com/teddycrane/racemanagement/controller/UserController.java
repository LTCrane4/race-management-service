package com.teddycrane.racemanagement.controller;

import com.teddycrane.racemanagement.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/user")
@RestController
public class UserController extends BaseController {
  public UserController() { super(); }

  @GetMapping("/{id}")
  public User getUser(@PathVariable String id) {
    logger.trace("getUser called");

    return new User();
  }
}
