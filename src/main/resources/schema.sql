CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    login varchar(255) UNIQUE NOT NULL,
    name varchar(255),
    email varchar(255) UNIQUE NOT NULL,
    birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS user_friends (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id int NOT NULL,
    friend_id int NOT NULL,
    is_confirmed boolean
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    release_date date NOT NULL,
    duration int NOT NULL,
    description varchar(200),
    mpa_rating_id int
);

CREATE TABLE IF NOT EXISTS film_likes (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    film_id int NOT NULL,
    user_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    film_id int NOT NULL,
    genre_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS film_mpa_ratings (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name varchar(5) UNIQUE NOT NULL
);

ALTER TABLE film_likes ADD FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE user_friends ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE film_likes ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE films ADD FOREIGN KEY (mpa_rating_id) REFERENCES film_mpa_ratings (id) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE user_friends ADD FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE film_genres ADD FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE film_genres ADD FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE ON UPDATE CASCADE ;