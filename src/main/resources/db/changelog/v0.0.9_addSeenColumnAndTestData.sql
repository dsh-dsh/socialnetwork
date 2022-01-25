--liquibase formatted sql
--changeset alexanderkochurov:add-seen-column-and-test-data

ALTER TABLE notification
ADD COLUMN IF NOT EXISTS seen boolean;

