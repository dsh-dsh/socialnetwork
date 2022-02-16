--liquibase formatted sql
--changeset alexanderkochurov:add-notifications-types

INSERT INTO notification_type
(name, code)
VALUES
('Новый пост','POST'),
('Комментарий к посту', 'POST_COMMENT'),
('Ответ на комментарий', 'COMMENT_COMMENT'),
('Запрос дружбы', 'FRIEND_REQUEST'),
('Личное сообщение', 'MESSAGE'),
('День рождения', 'FRIEND_BIRTHDAY');
