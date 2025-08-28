create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table comments(
    id bigserial,
    book_id bigint references books(id) on delete cascade,
    text varchar(255),
    primary key(id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table users (
    id bigserial,
    username varchar(50),
    password varchar(100),
    role varchar(20)
);

insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(book_id,text)
values (2,'Nice book'),
       (3,'Very good'),
       (3,'Boring');

insert into users(username,password,role)
values('user','$2a$12$LXP.6OLtXOvIaxqVUAg9IOve.Ogc/VAQZbkwpaq8M5veQ.kvN9X4C','USER')