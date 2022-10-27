package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.CustomRowMapper;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private static final String SQL_VALIDATE = "SELECT COUNT(*) AS count " +
            "FROM mpa " +
            "WHERE mpa_id = ?";
    private static final String SQL_GET_BY_ID = "SELECT * " +
            "FROM mpa " +
            "WHERE mpa_id = ?";
    @Override
    public Mpa getMpa(int id) {
        log.info("MpaDbStorage: Получен запрос на получение Mpa с id: {}.", id);
        Mpa mpa = jdbcTemplate.queryForObject(SQL_GET_BY_ID, CustomRowMapper::mapRowToMpa, id);
        log.trace("MpaDbStorage: Получен Mpa: {} {}", mpa.getId(), mpa.getName());
        return mpa;
    }
    @Override
    public List<Mpa> findAll() {
        log.info("MpaDbStorage: Получен запрос на получение всех Mpa.");
        String sqlToAllMpa = "SELECT * " +
                "FROM mpa";
        List<Mpa> listOfMpa = jdbcTemplate.query(sqlToAllMpa, CustomRowMapper::mapRowToMpa);
        return listOfMpa;
    }

    @Override
    public boolean validateDataExists(Integer id) {
        int count = jdbcTemplate.queryForObject(SQL_VALIDATE, CustomRowMapper::mapRowCount, id);
        return count != 0;
    }
}
