CREATE TABLE IF NOT EXISTS "language"(
	id SERIAL PRIMARY KEY NOT NULL,
	title VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS country(
	id SERIAL PRIMARY KEY NOT NULL,
	title VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS city(
	id SERIAL PRIMARY KEY NOT NULL,
	title VARCHAR(255)
);

INSERT INTO "language"
(title)
VALUES
('Русский');

INSERT INTO country
(title)
VALUES
('Россия'),
('Израиль');

INSERT INTO city
(title)
VALUES
('Москва'),
('Краснодар'),
('Омск'),
('Пермь'),
('Серов'),
('Тель-Авив');
