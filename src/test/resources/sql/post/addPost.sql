alter sequence post_id_seq restart 10;
insert into post (time, author_id, title,  post_text, is_blocked)
values ('2021-11-10T14:55:56', 1, 'test title', 'test post text', false);

alter sequence post_comment_id_seq restart 10;
insert into post_comment (time, post_id, author_id, comment_text, is_blocked)
values ('2021-12-10T14:55:56', 10, 2, 'test comment 1', false),
       ('2021-12-10T14:55:56', 10, 2, 'test comment 2', false);

alter sequence post_like_id_seq restart 10;
insert into post_like (time, person_id, post_id)
values ('2021-12-10T14:55:56', 1, 10);