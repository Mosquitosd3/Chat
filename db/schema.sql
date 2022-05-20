create table roles(
    id serial primary key,
    name varchar(255)
);
insert into roles(name) values ('Admin'), ('Moderator'), ('User');

create table persons(
    id serial primary key,
    username varchar(255) not null unique,
    role_id int references roles(id)
);
insert into persons(username, role_id) values ('user-1', 1), ('user-2', 3), ('user-3', 3);

create table rooms(
    id serial primary key,
    name varchar(255)
);
insert into rooms(name) values ('room-1'), ('room-2'), ('room-3');

create table messages(
    id serial primary key,
    message varchar(255),
    created timestamp not null default now(),
    person_id int references persons(id)
);

create table rooms_messages(
    room_id int references rooms(id),
    messages_id int references messages(id)
);
