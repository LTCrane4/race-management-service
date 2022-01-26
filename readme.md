# Race Management Service

[![CircleCI](https://circleci.com/gh/LTCrane4/race-management-service/tree/main.svg?style=svg)](https://circleci.com/gh/LTCrane4/race-management-service/tree/main)
[![codecov](https://codecov.io/gh/LTCrane4/race-management-service/branch/main/graph/badge.svg?token=SZ2DQAXYS6)](https://codecov.io/gh/LTCrane4/race-management-service)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit&logoColor=white)](https://github.com/pre-commit/pre-commit)


## Overview

This is a Java/Spring Boot service designed to manage bicycle races

## Installation

This repository utilizes pre-commit hooks to enforce code style and security.
To install the hooks defined in `.pre-commit-config.yaml`, install `pre-commit`
with `pip install pre-commit` and then run `pre-commit install` in the root of the repository.
Optionally, `pre-commit run --all-files` can be invoked to run the pre-commit checks on all files,
although this can create large diffs.

This project also uses gradle as a build tool, building target at Java 17, as well as Docker and
Docker compose for a cross-platform build solution.  

Finally, this project is utilizing MySQL for a database solution.  MySQL workbench is
strongly recommended as a database management solution

## Local Development

To enable local development, a few configuration steps are required to enforce application security.

1. Create a `.env` file in the root of the repository, with the following entries:

```dotenv
  SECRET_KEY=<base64_secret_key>
  MYSQL_DATABASE=<database_name>
  DB_USER=<db_username>
  DB_PASSWORD=<your_db_pw>
  DB_ROOT_PW=<root_db_pw>
  TEST_USER=testuser
  TEST_USER_PW=<encoded_password>
  TEST_ADMIN=testadmin
  TEST_ADMIN_PW=<encoded_password>
```
2. Open a terminal, and generate a secret key for token signing with the following:
`openssl rand -base64 64`. Take the result and insert it into the `SECRET_KEY` field.

3. Create a database name, root password, username and password, and enter these values into `.env`.

4. Create a test user and test admin (recommended values are `testuser` and `testadmin`).
Enter these values into `.env`.
