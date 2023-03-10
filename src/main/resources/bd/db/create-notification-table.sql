--liquibase formatted sql
--changeset Olgakargan:create-notifications
create table notifications
(
    id      bigint generated by default as identity
        primary key,
    text    varchar(255),
    time    timestamp,
    user_id bigint
        constraint user_id_fk
            references users,
    number  bigint
);
create index notifications_time_idx on notifications(time);
alter table notifications
    owner to "myUser";