create table if not exists "notification_setting" (
	id serial PRIMARY KEY not null,
	person_id int references person(id),
	notification_type_code varchar(255),
	permission BOOLEAN
);