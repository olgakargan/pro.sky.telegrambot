--liquibase formatted sql

--changeset Olgakargan:delete-column-notifications_number

alter table users
drop column notifications_number;