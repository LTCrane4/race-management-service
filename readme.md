# Race Management Service

[![CircleCI](https://circleci.com/gh/LTCrane4/race-management-service/tree/main.svg?style=svg)](https://circleci.com/gh/LTCrane4/race-management-service/tree/main)
[![codecov](https://codecov.io/gh/LTCrane4/race-management-service/branch/main/graph/badge.svg?token=SZ2DQAXYS6)](https://codecov.io/gh/LTCrane4/race-management-service)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit&logoColor=white)](https://github.com/pre-commit/pre-commit)


## Overview

This is a Java/Spring Boot service designed to manage bicycle races

## Installation

This repository utilizes pre-commit hooks to enforce code style and security.  To install the hooks defined in `.pre-commit-config.yaml`, install `pre-commit` with `pip install pre-commit` and then run `pre-commit install` in the root of the repository.  Optionally, `pre-commit run --all-files` can be invoked to run the pre-commit checks on all files, although this can create large diffs.

This project also uses gradle as a build tool, building target at Java 17, as well as Docker and Docker compose for a cross-platform build solution.  

Finally, this project is utilizing MySQL for a database solution.  MySQL workbench is strongly recommended as a database management solution

## Local Development

To enable local development, a few configuration steps are required to enforce application security.  
