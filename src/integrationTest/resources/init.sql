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

-- Seed User Data

--      UUID                                    username    password    email              first/last_name  user_type

INSERT INTO user (id, username, password, email, first_name, last_name, user_type)
VALUES('bf68eea7-25a6-4dbf-867b-0829e308bbdd', 'testuser', 'password', 'email@fake.fake', 'Test', 'User', 'USER');
