INSERT INTO users (login, name, email, birthday)
VALUES ('TestUser_1', 'TU1', 'testuser1@someserver.com', '1990-01-01'),
       ('TestUser_2', 'TU2', 'testuser2@someserver.com', '1990-02-02'),
       ('TestUser_3', 'TU3', 'testuser3@someserver.com', '1990-03-03');

INSERT INTO user_friends (user_id, friend_id, is_confirmed)
VALUES (1, 2, true), (2, 1, true), (1, 3, false);

INSERT INTO films (name, release_date, duration, description, mpa_rating_id)
VALUES
    ('Иван Васильевич меняет профессию', '1973-09-17', 88, 'Инженер-изобретатель Тимофеев сконструировал машину времени, которая соединила его квартиру с далеким шестнадцатым веком - точнее, с палатами государя Ивана Грозного.', 1),
    ('Винни Пух', '1969-07-19', 63, 'Первый фильм из серии о забавном медвежонке и его друзьях.', 1),
    ('Москва слезам не верит', '1980-02-11', 150, 'Москва, 1950-е годы. Три молодые провинциалки приезжают в Москву в поисках того, что ищут люди во всех столицах мира — любви, счастья и достатка.', 3);

INSERT INTO film_genres (film_id, genre_id)
VALUES
    (1, 1), (1, 2),
    (2, 3),
    (3, 1), (3, 2);

INSERT INTO film_likes (film_id, user_id)
VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 1), (2, 2),
    (3, 1);