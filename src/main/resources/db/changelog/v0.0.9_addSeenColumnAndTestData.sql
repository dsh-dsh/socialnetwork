--liquibase formatted sql
--changeset alexanderkochurov:add-seen-column-and-test-data

ALTER TABLE notification
ADD COLUMN IF NOT EXISTS seen boolean;

INSERT INTO notification
(type_id, sent_time, person_id, entity_id, contact, seen)
VALUES
(1, '2021-11-12 14:55:56', 2, '2', 'p2@mail.ru', false)