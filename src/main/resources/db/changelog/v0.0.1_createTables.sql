--liquibase formatted sql
--changeset alexanderkochurov:create-tables
create table if not exists person(
	id serial PRIMARY KEY not null,
	first_name varchar(255),
	last_name varchar(255),
	reg_date timestamp,
	birth_date timestamp,
	e_mail varchar(255),
	phone varchar(255),
	password varchar(255),
	photo TEXT,
	about TEXT,
	city varchar(255),
	country varchar(255),
	confirmation_code varchar(255),
	is_approved BOOLEAN,
	messages_permission varchar(255),
	last_online_time timestamp,
	is_blocked BOOLEAN,
    is_deleted BOOLEAN
);

create table if not exists "user" (
	id serial PRIMARY KEY not null,
	name varchar(255),
	e_mail varchar(255),
	password varchar(255),
	"type" varchar(255)
);

create table if not exists friendship_status(
	id serial PRIMARY KEY not null,
	"time" timestamp not null,
	name varchar(255),
	code varchar(255)
);

create table if not exists friendship (
	id serial PRIMARY KEY not null,
	status_id int references friendship_status(id),
	src_person_id int references person(id),
	dst_person_id int references person(id)
);


create table if not exists message(
	id serial PRIMARY KEY not null,
	"time" timestamp,
	author_id int references person(id),
	recipient_id int references person(id),
	message_text TEXT,
	read_status varchar(255)
);


create table if not exists post(
	id serial PRIMARY KEY not null,
	time timestamp,
	author_id int references person(id),
	title TEXT,
	post_text TEXT,
	is_blocked BOOLEAN
);


create table if not exists tag(
	id serial PRIMARY KEY not null,
	tag varchar(255)
);


create table if not exists post2tag (
	id serial PRIMARY KEY not null,
	post_id int references post(id),
	tag_id int references tag(id)
);

create table if not exists post_like(
	id serial PRIMARY KEY not null,
	time timestamp,
	person_id int references person(id),
	post_id int references post(id)
);

create table if not exists post_file(
	id serial PRIMARY KEY not null,
	post_id int references post(id),
	name varchar(255),
	"path" text
);

create table if not exists post_comment(
	id serial PRIMARY KEY not null,
	time timestamp,
	post_id int references post(id),
	parent_id int references post_comment(id),
	author_id int references person(id),
	comment_text TEXT,
	is_blocked BOOLEAN
);

create table if not exists block_history(
	id serial PRIMARY KEY not null,
	time timestamp,
	person_id int references person(id),
	post_id int references post(id),
	comment_id int references post_comment(id),
	"action" varchar(255)
);

create table if not exists notification_type(
	id serial PRIMARY KEY not null,
	name varchar(255),
	code varchar(255)
);

create table if not exists notification(
	id serial PRIMARY KEY not null,
	"type_id" int references notification_type(id),
	sent_time timestamp,
	person_id int references person(id),
	entity_id varchar(255),
	contact varchar(255)
);
