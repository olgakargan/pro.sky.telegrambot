--liquibase formatted sql

--changeset OlgaKargan:create-users-table

create table users
(
    id           bigint generated by default as identity
        primary key,
    chat_id      bigint not null,
    name                 varchar(255),
    notifications_number bigint default 0
);


alter table users
    owner to "myUser";