--liquibase formatted sql
--changeset alexanderkochurov:create-testData

-- users accounts
INSERT INTO person
(first_name, last_name, reg_date, birth_date, e_mail, phone, password, about, city, country, confirmation_code, is_approved, messages_permission, last_online_time, is_blocked, is_deleted)
VALUES('Василий', 'Пупкин', '2021-11-10T14:55:56', '1990-11-16T14:55:56', 'p1@mail.ru', '89991234567', '$2y$12$NKArmf9agtEQw7rPDN4zb.rE90zeewGAUWNRkSrYW662FwL77NyCS', 'Родился в небольшой, но честной семье', 'Серов', 'Россия', '123456', true, 'ALL', '2021-11-16T14:55:56', false, false),
('Иван', 'Иванов', '2021-11-11T14:55:56', '1992-11-16T14:55:56', 'p2@mail.ru', '89992345678', '$2y$12$NKArmf9agtEQw7rPDN4zb.rE90zeewGAUWNRkSrYW662FwL77NyCS', 'Родился в большой, но нечестной семье', 'Москва', 'Россия', '123456', true, 'ALL', '2021-11-16T14:55:56', false, false),
('Феликс', 'Абрамов', '2021-11-12T14:55:56', '1994-11-16T14:55:56', 'p3@mail.ru', '89991234567', '$2y$12$NKArmf9agtEQw7rPDN4zb.rE90zeewGAUWNRkSrYW662FwL77NyCS', 'Родился в небольшой и нечестной семье', 'Тель-Авив', 'Израиль', '123456', true, 'ALL', '2021-11-16T14:55:56', false, false);

-- posts
INSERT INTO post
    (time, author_id, title, post_text, is_blocked)
VALUES ('2021-11-10 14:55:56', 1, 'My first post about Java Collections', 'Java Collections are so fun and easy!',
        false);

INSERT INTO post
    (time, author_id, title, post_text, is_blocked)
VALUES ('2021-11-10 14:55:56', 2, 'All things about my life', 'Nobody actually even care!', true);

INSERT INTO post
    (time, author_id, title, post_text, is_blocked)
VALUES ('2021-11-12 14:55:56', 3, 'Third test post', 'Nothing to see here', false);


-- comments
INSERT INTO post_comment
    (time, post_id, parent_id, author_id, comment_text, is_blocked)
VALUES ('2021-11-11T14:55:56', 1, null, 1, 'No it is not', false);

INSERT INTO post_comment
    (time, post_id, parent_id, author_id, comment_text, is_blocked)
VALUES ('2021-11-12T14:55:56', 1, 1, 2, 'Learn about it more and you will see ;)', false);

INSERT INTO post_comment
    (time, post_id, parent_id, author_id, comment_text, is_blocked)
VALUES ('2021-11-13T14:55:56', 2, null, 2, 'Stop this sh#t man', false);

INSERT INTO post_comment
    (time, post_id, parent_id, author_id, comment_text, is_blocked)
VALUES ('2021-11-14T14:55:56', 2, null, 3, 'Agreed', false);

INSERT INTO post_comment
    (time, post_id, parent_id, author_id, comment_text, is_blocked)
VALUES ('2021-11-15T14:55:56', 3, null, 3, 'Bla-bla-bla', false);

INSERT INTO post_comment
    (time, post_id, parent_id, author_id, comment_text, is_blocked)
VALUES ('2021-11-16T14:55:56', 3, 5, 1, 'qwljkqjwnpnqgiknqokngl', false);


--likes

INSERT INTO post_like
    (time, post_id, person_id)
VALUES ('2021-11-14 14:55:56', 2, 3);

INSERT INTO post_like
    (time, post_id, person_id)
VALUES ('2021-11-14 14:55:56', 1, 2);

-- tags
INSERT INTO tag (tag)
VALUES ('Java Collections');
INSERT INTO tag (tag)
VALUES ('Life');
INSERT INTO tag (tag)
VALUES ('Test');
