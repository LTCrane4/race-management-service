use test;

create table user
(
    id         varchar(255) not null
        primary key,
    email      varchar(255) null,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    password   varchar(255) null,
    user_type  varchar(255) null,
    username   varchar(255) null
);

create table racer
(
    id                varchar(255) not null primary key,
    bib_number        int          not null,
    category          varchar(255) null,
    email             varchar(255) null,
    first_name        varchar(255) null,
    last_name         varchar(255) null,
    middle_name       varchar(255) null,
    phone_number      varchar(255) null,
    team_name         varchar(255) null,
    created_timestamp datetime(6)  null,
    updated_timestamp datetime(6)  null
);

CREATE TABLE race
(
    id       varchar(255) not null primary key,
    category varchar(255) null,
    name     varchar(255) null
);

CREATE TABLE race_racers
(
    race_id   varchar(255) not null,
    racers_id varchar(255) not null,
    FOREIGN KEY (race_id) REFERENCES race (id),
    FOREIGN KEY (racers_id) REFERENCES racer (id)
);

-- Seed User Data

--      UUID                                    username    password    email              first/last_name  user_type

INSERT INTO user (id, username, password, email, first_name, last_name, user_type)
VALUES ('bf68eea7-25a6-4dbf-867b-0829e308bbdd', 'testuser', 'password', 'email@fake.fake', 'Test',
        'User', 'USER');

-- Seed Racer Data
--      UUID                                  bib    cat   email               first/middle/lastName           phone           Team         created                        updated
INSERT INTO racer (id, bib_number, category, email, first_name, last_name, middle_name,
                   phone_number, team_name, created_timestamp, updated_timestamp)
VALUES ('126c9659-8b47-4c27-a10c-d91f2651c4e9', 1, 'CAT1', 'test@testteam.com', 'Test', 'Racer',
        'Middlename', '123-456-7890', 'Test Team', '2022-01-27 19:42:42.770000',
        '2022-01-27 19:42:42.770000');

INSERT INTO racer (id, bib_number, category, email, first_name, last_name, middle_name,
                   phone_number, team_name, created_timestamp, updated_timestamp)
VALUES ('ef70cd1c-edf7-48cb-9042-2c7b093fd28b', 2, 'CAT2', 'other@testteam.com', 'Other', 'Racer',
        'Clyde', '444-555-6666', 'Test Team', '2022-01-27 19:42:42.770000',
        '2022-01-27 19:42:42.770000');

-- Seed Race data
--       UUID                                    cat     name
INSERT INTO race (id, category, name)
VALUES ('7E51E4B2-5A96-4828-AF09-F6A574B8FC8A', 'CAT1', 'Test Race');

-- Seed race_racers data
-- 
INSERT INTO race_racers (race_id, racers_id)
VALUES ('7E51E4B2-5A96-4828-AF09-F6A574B8FC8A', 'ef70cd1c-edf7-48cb-9042-2c7b093fd28b');
