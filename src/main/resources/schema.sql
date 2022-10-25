DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS genre_line CASCADE;
DROP TABLE IF EXISTS users_likes_film CASCADE;
DROP TABLE IF EXISTS friends_line CASCADE;

CREATE TABLE mpa (
    mpa_id INTEGER PRIMARY KEY NOT NULL,
    rating varchar
);

CREATE TABLE genre (
    genre_id INTEGER PRIMARY KEY NOT NULL,
    genre_name varchar
);

CREATE TABLE users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    email varchar NOT NULL,
    login varchar NOT NULL,
    name varchar NOT NULL,
    birthday date NOT NULL
);

CREATE TABLE films (
    film_id INTEGER
        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar NOT NULL,
    description varchar(200) NOT NULL,
    release_date date NOT NULL,
    duration INTEGER NOT NULL,
    rate INTEGER NOT NULL,
    mpa_id INTEGER,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id)
);

CREATE TABLE genre_line (
    film_id INTEGER REFERENCES films (film_id) NOT NULL,
    genre_id INTEGER REFERENCES genre (genre_id) NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id)

);

CREATE TABLE users_likes_film (
    film_id INTEGER REFERENCES films (film_id) NOT NULL,
    user_id INTEGER REFERENCES users (user_id) NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)

);

CREATE TABLE friends_line (
    user_id INTEGER REFERENCES users (user_id) NOT NULL,
    friends_id INTEGER REFERENCES users (user_id) NOT NULL,
    PRIMARY KEY (user_id, friends_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (friends_id) REFERENCES users (user_id)
);