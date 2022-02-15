DELETE FROM person where e_mail = 'nikita.r200019@gmail.com';

INSERT INTO person
(first_name, last_name, reg_date, birth_date, e_mail, phone, password, about, city, country, confirmation_code, is_approved, messages_permission, last_online_time, is_blocked, is_deleted)
VALUES
    ('Some', 'Person', '2021-11-10T14:55:56', '1990-11-16T14:55:56', 'nikita.r200019@gmail.com', '89991234567', '$2y$12$NKArmf9agtEQw7rPDN4zb.rE90zeewGAUWNRkSrYW662FwL77NyCS', 'Родился в небольшой, но честной семье', 'Серов', 'Россия', '123456', true, 'ALL', '2021-11-16T14:55:56', false, false);
