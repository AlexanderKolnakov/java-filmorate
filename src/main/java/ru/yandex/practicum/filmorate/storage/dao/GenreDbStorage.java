package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.CustomRowMapper;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private static final String SQL_VALIDATE = "SELECT COUNT(genre_id) AS count " +
            "FROM genre " +
            "WHERE genre_id = ?";
    private static final String SQL_TO_ALL_GENRE = "SELECT * " +
            "FROM genre";
    private static final String SQL_GET_BY_ID = "SELECT  * "+
            "FROM genre " +
            "WHERE genre_id = ?";

    @Override
    public List<Genre> findAll() {
        log.info("GenreDbStorage: Получен запрос на получение всех Genre.");
        List<Genre> listOfGenre = jdbcTemplate.query(SQL_TO_ALL_GENRE, CustomRowMapper::mapRowToGenre);
        return listOfGenre;
    }

    @Override
    public Genre getGenre(int id) {
        log.info("GenreDbStorage: Получен запрос на получение Mpa с id: {}.", id);
        Genre genre = jdbcTemplate.queryForObject(SQL_GET_BY_ID, CustomRowMapper::mapRowToGenre, id);
        log.trace("GenreDbStorage: Получен Genre: {} {}", genre.getId(), genre.getName());
        return genre;
    }

    @Override
    public boolean validateDataExists(Integer id) {
        int count = jdbcTemplate.queryForObject(SQL_VALIDATE, CustomRowMapper::mapRowCount, id);
        return count != 0;
    }
}
