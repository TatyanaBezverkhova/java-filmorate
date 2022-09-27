-- INSERT INTO genre (name)
-- VALUES ('Комедия'),
--        ('Драма'),
--        ('Мультфильм'),
--        ('Триллер'),
--        ('Документальный'),
--        ('Боевик');
--
-- INSERT INTO mpa (name)
-- VALUES ('G'),
--        ('PG'),
--        ('PG-13'),
--        ('R'),
--        ('NC-17');

INSERT INTO film (name, description, releaseDate, duration)
VALUES ('film One', 'description One', '1995-12-28', '35'),
       ('film Two', 'description Two', '1900-12-18', '40'),
       ('film Three', 'description Three', '2005-11-8', '130');

INSERT INTO users (name, login, email, birthday)
VALUES ('Tanya', 'Snoopy', 'tanya@snoopy.ru', '1990-06-18'),
       ('Mikel', 'Miki', 'mikel@miki.com', '1995-09-28'),
       ('Donald', 'Duck', 'Donald@duck.com', '1988-01-01');

INSERT INTO film_genre (film_id, genre_id)
VALUES ('1', '1'),
       ('1', '3'),
       ('2', '3'),
       ('3', '4'),
       ('3', '6');

INSERT INTO film_mpa (film_id, mpa_id)
VALUES ('3', '5'),
       ('1', '2'),
       ('2', '4');

INSERT INTO friends (user_id, friend_id)
VALUES ('1', '2'),
       ('2', '1'),
       ('1', '3'),
       ('3', '2');

INSERT INTO likes (film_id, user_id)
VALUES ('2', '2'),
       ('2', '1'),
       ('2', '3'),
       ('1', '3');

