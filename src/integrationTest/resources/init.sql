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
    constraint UK_rdmw93dcho5bnks5hy0rdt40e unique (racers_id),
    constraint FK8k14l732x67t5kulic5aibb8b foreign key (racers_id) references racer (id),
    constraint FKl5c63waba1wbrk7567lqt2dq7 foreign key (race_id) references race (id)
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
        '2022-01-27 19:42:42.770000')
