DROP TABLE IF EXISTS _user;
CREATE TABLE _user (
    id uuid NOT NULL,
    created_timestamp timestamp without time zone,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255),
    status character varying(255),
    updated_timestamp timestamp without time zone,
    user_type character varying(255),
    username character varying(255)
);

DROP TABLE IF EXISTS race_racers;
CREATE TABLE race_racers (
    race_id character varying(255) NOT NULL,
    racers_id character varying(255) NOT NULL
);

DROP TABLE IF EXISTS race;
CREATE TABLE race (
    id character varying(255) NOT NULL,
    category character varying(255),
    created_timestamp timestamp without time zone,
    event_date date,
    finish_time time without time zone,
    name character varying(255),
    start_time time without time zone,
    updated_timestamp timestamp without time zone
);

DROP TABLE IF EXISTS racer;
CREATE TABLE racer (
    id character varying(255) NOT NULL,
    bib_number integer NOT NULL,
    category character varying(255),
    created_timestamp timestamp without time zone,
    email character varying(255),
    first_name character varying(255),
    is_deleted boolean NOT NULL,
    last_name character varying(255),
    middle_name character varying(255),
    phone_number character varying(255),
    team_name character varying(255),
    updated_timestamp timestamp without time zone
);

ALTER TABLE ONLY _user
    ADD CONSTRAINT _user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY race
    ADD CONSTRAINT race_pkey PRIMARY KEY (id);

ALTER TABLE ONLY racer
    ADD CONSTRAINT racer_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.race_racers
    ADD CONSTRAINT fk8k14l732x67t5kulic5aibb8b FOREIGN KEY (racers_id) REFERENCES public.racer(id);

ALTER TABLE ONLY public.race_racers
    ADD CONSTRAINT fkl5c63waba1wbrk7567lqt2dq7 FOREIGN KEY (race_id) REFERENCES public.race(id);

-- Seed User Data
--      UUID                                    username    password    email              first/last_name  user_type status created_timestamp updated_timestamp
INSERT INTO _user (id, username, password, email, first_name, last_name, user_type, status, created_timestamp, updated_timestamp)
    VALUES ('bf68eea7-25a6-4dbf-867b-0829e308bbdd', 'testuser', 'password', 'email@fake.fake', 'Test',
            'User', 'USER', 'ACTIVE', '2022-04-18 18:32:58.459075', '2022-04-18 18:32:58.459075');

---- Seed Racer Data
----      UUID                                  bib    cat   email               first/middle/lastName           phone           Team         created                        updated is_deleted
INSERT INTO racer (id, bib_number, category, email, first_name, last_name, middle_name,
                       phone_number, team_name, created_timestamp, updated_timestamp, is_deleted)
VALUES ('126c9659-8b47-4c27-a10c-d91f2651c4e9', 1, 'CAT1', 'test@testteam.com', 'Test', 'Racer',
            'Middlename', '123-456-7890', 'Test Team', '2022-01-27 19:42:42.770000',
            '2022-01-27 19:42:42.770000', false);

INSERT INTO racer (id, bib_number, category, email, first_name, last_name, middle_name,
                       phone_number, team_name, created_timestamp, updated_timestamp, is_deleted)
VALUES ('ef70cd1c-edf7-48cb-9042-2c7b093fd28b', 2, 'CAT2', 'other@testteam.com', 'Other', 'Racer',
            'Clyde', '444-555-6666', 'Test Team', '2022-01-27 19:42:42.770000',
            '2022-01-27 19:42:42.770000', false);

---- Seed Race data
----       UUID                                    cat     name
INSERT INTO race (id, category, name)
    VALUES ('7E51E4B2-5A96-4828-AF09-F6A574B8FC8A', 'CAT1', 'Test Race');

-- Seed race_racers data
--
INSERT INTO race_racers (race_id, racers_id)
    VALUES ('7E51E4B2-5A96-4828-AF09-F6A574B8FC8A', 'ef70cd1c-edf7-48cb-9042-2c7b093fd28b');
