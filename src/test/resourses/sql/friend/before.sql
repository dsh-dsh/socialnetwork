DELETE FROM friendship;

DELETE FROM friendship_status;

ALTER SEQUENCE friendship_id_seq RESTART;

ALTER SEQUENCE friendship_status_id_seq RESTART;

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