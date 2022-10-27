package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.CustomRowMapper;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("FilmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private static final String SQL_GET_BY_ID = "SELECT f.film_id, f.name, f.description, " +
            "f.release_date, f.duration, f.rate, f.mpa_id, m.rating " +
            "FROM films AS f " +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "WHERE film_id = ?";
    private static final String SQL_UPDATE_FILM = "UPDATE films SET " +
            "name = ?, description = ?, release_date = ?, " +
            "duration = ?, rate = ?, mpa_id = ? " +
            "WHERE film_id = ?";
    private static final String SQL_VALIDATE = "SELECT COUNT(*) AS count " +
            "FROM films " +
            "WHERE film_id = ?";
    private static final String SQL_TO_LIKES = "SELECT user_id " +
            "FROM users_likes_film " +
            "WHERE film_id = ?";
    private static final String SQL_TO_ALL_FILMS = "SELECT * " +
            "FROM films as f " +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id ";
    private static final String SQL_TO_GENRE = "SELECT gl.genre_id, g.genre_name " +
            "FROM genre_line AS gl " +
            "LEFT JOIN genre AS g ON gl.genre_id = g.genre_id " +
            "WHERE film_id = ?";

    @Override
    public List<Film> findAll() {
        log.info("FilmDbStorage: Получен запрос на получение всех фильмов.");
        List<Film> listOfFilms = jdbcTemplate.query(SQL_TO_ALL_FILMS, CustomRowMapper::mapRowToFilm);

        listOfFilms.forEach(this::setLikes);
        listOfFilms.forEach(this::setGenre);
        return listOfFilms;
    }

    @Override
    public Film create(Film film) {
        log.info("FilmDbStorage: Получен запрос на добавление фильма {}.", film.getName());

        SimpleJdbcInsert filmJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        int id = filmJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(id);
        log.info("FilmDbStorage: добавлен фильм с id - {}.", id);
        updateGenre(film);
        return getFilm(film.getId());
    }

    @Override
    public Film update(Film film) {
        log.info("FilmDbStorage: Получен запрос на обновление фильма  с id: {}.", film.getId());

        jdbcTemplate.update(SQL_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        deleteGenre(film);
        updateGenre(film);
        log.info("FilmDbStorage: Фильм с id: {} успешно обновлен.", film.getId());
        return getFilm(film.getId());
    }

    @Override
    public Film getFilm(int filmsID) {
        log.info("FilmDbStorage: Получен запрос на получение фильма с id: {}.", filmsID);

        Film film = jdbcTemplate.queryForObject(SQL_GET_BY_ID, CustomRowMapper::mapRowToFilm, filmsID);
        log.trace("FilmDbStorage: Получен фильм: {} {}", film.getId(), film.getName());
        Set<Integer> likes = new HashSet<>(jdbcTemplate.query(SQL_TO_LIKES,
                CustomRowMapper::mapRowToLikes, filmsID));
        film.setLikes(likes);

        Set<Genre> genre = new HashSet<>(jdbcTemplate.query(SQL_TO_GENRE,
                CustomRowMapper::mapRowToGenre, filmsID));
        film.setGenres(genre);
        return film;
    }

    @Override
    public boolean validateDataExists(int id) {
        int count = jdbcTemplate.queryForObject(SQL_VALIDATE, CustomRowMapper::mapRowCount, id);
        return count != 0;
    }

    private void updateGenre(Film film) {
        log.info("FilmDbStorage: Обновление жанров фильма с id: {}.", film.getId());
        String sqlGenre = "INSERT INTO genre_line(film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlGenre, film.getId(), genre.getId());
        }
    }

    public void setLikes(Film film) {
        Set<Integer> likes = new HashSet<>(jdbcTemplate.query(SQL_TO_LIKES,
                CustomRowMapper::mapRowToLikes, film.getId()));
        film.setLikes(likes);
    }

    private void setGenre(Film film) {
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(SQL_TO_GENRE,
                CustomRowMapper::mapRowToGenre, film.getId()));
        film.setGenres(genres);
    }

    private void deleteGenre(Film film) {
        String sqlDeleteGenre = "DELETE FROM genre_line WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteGenre, film.getId());
    }
}
