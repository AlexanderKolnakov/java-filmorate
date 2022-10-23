package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.CustomRowMapper;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("FilmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_BY_ID = "SELECT f.film_id, f.name, f.description, " +
            "f.release_date, f.duration, f.rate, f.mpa_id, m.rating " +
            "FROM films AS f " +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "WHERE film_id = ?";


    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);


    @Override
    public List<Film> findAll() {
//        String sql = "select * from films ";
//        List<Film> listOfFilms = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
//        return listOfFilms;
        return null;
    }

    @Override
    public Film create(Film film) throws ValidateException {

        log.info("FilmDbStorage: Получен запрос к хранилищу на добавление фильма {}.", film.getName());

        SimpleJdbcInsert filmJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

//        SimpleJdbcInsert mpaJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("mpa");
//
//        mpaJdbcInsert.execute(film.getMpa().toMap());

        int id = filmJdbcInsert.executeAndReturnKey(film.toMap()).intValue();

        film.setId(id);

        log.info("FilmDbStorage: В хранилище добавлен фильм с id - {}.", id);
        updateGenre(film);

        return film;
    }

    @Override
    public Film update(Film film) throws ValidateException, IDException {
        return null;
//        return new Film(1, "2", "fw", LocalDate.now(), 1, new Mpa(1, "dw"));
    }

    @Override
    public Film getFilm(int filmsID) {
        log.info("FilmDbStorage: Получен запрос к хранилищу на получение фильма с id: {}.", filmsID);

        Film film = jdbcTemplate.queryForObject(SQL_GET_BY_ID, CustomRowMapper::mapRowToFilm, filmsID);

        log.trace("FilmDbStorage: Получен фильм: {} {}", film.getId(), film.getName());

        String sqlToLikes = "SELECT user_id " +
                    "FROM users_likes_film " +
                    "WHERE film_id = ?";

        Set<Integer> likes = new HashSet<>(jdbcTemplate.query(sqlToLikes, CustomRowMapper::mapRowToLikes, filmsID));
        film.setLikes(likes);

        String sqlToGenre = "SELECT gl.genre_id, g.genre_name " +
                "FROM genre_line AS gl " +
                "LEFT JOIN genre AS g ON gl.genre_id = g.genre_id " +
                "WHERE film_id = ?";

        Set<Genre> genre = new HashSet<>(jdbcTemplate.query(sqlToGenre, CustomRowMapper::mapRowToGenre, filmsID));
        film.setGenres(genre);

        return film;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
//        Integer id = rs.getInt("film_id");
//        String  name = rs.getString("name");
//        String  description = rs.getString("description");
//        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
//        Integer  duration = rs.getInt("duration");
//        String  rating = rs.getString("rating");
//        List<String> genre = new LinkedList<>();
//
//        return new Film(id, name, description, releaseDate, duration, rating, genre);

        return null;
//        return new Film(1, "2", "fw", LocalDate.now(), 1, new Mpa(1, "dw"));

    }


    private void updateGenre(Film film) {
        int id = film.getId();
        log.info("FilmDbStorage: Получен запрос на обновление жанров фильма с id: {}.", id);

        String sql = "INSERT INTO genre_line(film_id, genre_id) VALUES (?, ?)";

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sql, id, genre.getGenreId());
        }
    }
}
