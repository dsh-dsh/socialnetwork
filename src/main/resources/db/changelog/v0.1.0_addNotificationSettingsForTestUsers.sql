--liquibase formatted sql
--changeset alexanderkochurov:add-notification-settings-for-test-users

INSERT INTO notification_setting
(person_id, notification_type_code, permission)
VALUES
(1, 'POST', true), (1, 'POST_COMMENT', true), (1, 'COMMENT_COMMENT', true), (1, 'FRIEND_REQUEST', true), (1, 'MESSAGE', true), (1, 'FRIEND_BIRTHDAY', true),
(2, 'POST', true), (2, 'POST_COMMENT', true), (2, 'COMMENT_COMMENT', true), (2, 'FRIEND_REQUEST', true), (2, 'MESSAGE', true), (2, 'FRIEND_BIRTHDAY', true),
(3, 'POST', true), (3, 'POST_COMMENT', true), (3, 'COMMENT_COMMENT', true), (3, 'FRIEND_REQUEST', true), (3, 'MESSAGE', true), (3, 'FRIEND_BIRTHDAY', true),
(4, 'POST', true), (4, 'POST_COMMENT', true), (4, 'COMMENT_COMMENT', true), (4, 'FRIEND_REQUEST', true), (4, 'MESSAGE', true), (4, 'FRIEND_BIRTHDAY', true),
(5, 'POST', true), (5, 'POST_COMMENT', true), (5, 'COMMENT_COMMENT', true), (5, 'FRIEND_REQUEST', true), (5, 'MESSAGE', true), (5, 'FRIEND_BIRTHDAY', true);