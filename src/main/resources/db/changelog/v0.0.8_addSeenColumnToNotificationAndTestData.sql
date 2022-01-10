--liquibase formatted sql
--changeset alexanderkochurov:add-Seen-Column-To-Notification-And-Test-Data
ALTER TABLE notification
ADD COLUMN seen BOOLEAN;

INSERT INTO notification(type_id, sent_time, person_id, entity_id, contact, seen)
VALUES
(1, '2022-01-01 14:55:56', 2, '2', 'p2@mail.ru', false);
