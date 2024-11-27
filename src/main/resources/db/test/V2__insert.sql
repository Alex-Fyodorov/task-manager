insert into roles (name)
values
('ROLE_USER'), ('ROLE_ADMIN');

insert into users (username, password, email)
values
('user1', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user1@gmail.com'),
('user2', '$2a$12$NnGbs88zdSPhBChQboBMfu7PyK08irFpkigGqEHyxb4xJOZHAQvfq', 'user2@gmail.com'),
('user3', '$2a$12$L3bFnBI5AUIJUjk3y7FYROCbtaSsqtCufb5xtof9DVOwe6968VgPa', 'user3@gmail.com');

insert into users_roles (user_id, role_id)
values
(1, 1), (2, 2), (3, 1), (3, 2);

insert into statuses (title) values
('в ожидании'),
('в процессе'),
('завершено');

insert into priorities (title) values
('высокий'),
('средний'),
('низкий');

insert into tasks (title, description, status_id, priority_id, author_id, executor_id)
values
('лампочка', 'поменять лампочку в коридоре второго этажа', 1, 2, 2, 3),
('туалет', 'помыть туалет, срочно', 1, 1, 2, 1);

insert into comments (text, task_id, author_id)
values
('лампочки кончились', 1, 3),
('купи', 1, 2),
('денег нет', 1, 3),
('не хочу!', 2, 1),
('сам мой!', 2, 1);