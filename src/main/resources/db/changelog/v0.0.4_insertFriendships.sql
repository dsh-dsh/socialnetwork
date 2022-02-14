INSERT INTO person
(first_name, last_name, reg_date, birth_date, e_mail, phone, password, about, city, country, confirmation_code, is_approved, messages_permission, last_online_time, is_blocked, is_deleted)
VALUES('Сергей', 'Сидоров', '2021-11-12T14:55:56', '1994-11-16T14:55:56', 'p4@mail.ru', '89991234567', '$2y$12$NKArmf9agtEQw7rPDN4zb.rE90zeewGAUWNRkSrYW662FwL77NyCS', 'Родился в небольшой и нечестной семье', 'Пермь', 'Россия', '123456', true, 'ALL', '2021-11-16T14:55:56', false, false);
INSERT INTO person
(first_name, last_name, reg_date, birth_date, e_mail, phone, password, about, city, country, confirmation_code, is_approved, messages_permission, last_online_time, is_blocked, is_deleted)
VALUES('Андрей', 'Вяземский', '2021-11-12T14:55:56', '1994-11-16T14:55:56', 'p5@mail.ru', '89991234567', '$2y$12$NKArmf9agtEQw7rPDN4zb.rE90zeewGAUWNRkSrYW662FwL77NyCS', 'Родился в небольшой и нечестной семье', 'Омск', 'Россия', '123456', true, 'ALL', '2021-11-16T14:55:56', false, false);


INSERT INTO friendship_status
(time, name, code)
VALUES
('2021-11-14T14:55:56', 'friend', 'FRIEND'),
('2021-11-14T15:55:56', 'friend', 'FRIEND'),
('2021-11-14T16:55:56', 'friend', 'FRIEND'),
('2021-11-14T17:55:56', 'friend', 'FRIEND'),
('2021-11-14T18:55:56', 'friend', 'FRIEND');

INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES
(1, 1, 2),
(2, 3, 1),
(3, 2, 4),
(4, 5, 3),
(5, 3, 4);
