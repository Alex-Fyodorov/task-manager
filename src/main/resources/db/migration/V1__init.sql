create table if not exists users (
 id        bigint primary key auto_increment,
 username  varchar(30) not null unique,
 password  varchar(80) not null,
 email     varchar(50) not null unique,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table if not exists roles (
 id        int primary key auto_increment,
 name      varchar(50) not null,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table if not exists users_roles (
 user_id   bigint not null,
 role_id   int not null,
 foreign key (user_id) references users (id),
 foreign key (role_id) references roles (id)
);

create table if not exists statuses (
 id         bigint primary key auto_increment,
 title      varchar(50) not null unique,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table if not exists priorities (
 id         bigint primary key auto_increment,
 title      varchar(50) not null unique,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table if not exists tasks (
 id         bigint primary key auto_increment,
 title      varchar(50) not null,
 description  varchar(255) not null,
 status_id  bigint not null references statuses (id),
 priority_id bigint not null references priorities (id),
 author_id  bigint not null references users (id),
 executor_id bigint not null references users (id),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table if not exists comments (
 id         bigint primary key auto_increment,
 text       varchar(255) not null,
 task_id    bigint not null references tasks (id),
 author_id  bigint not null references users (id),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);