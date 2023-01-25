--liquibase formatted sql

--changeset OlgaKargan:delete-column-number

alter table notifications
drop column number;