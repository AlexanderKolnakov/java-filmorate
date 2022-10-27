package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.CustomRowMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@Qualifier("UserDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private static final String SQL_VALIDATE = "SELECT COUNT(*) AS count " +
            "FROM users " +
            "WHERE user_id = ?";
    private static final String SQL_GET_BY_ID = "SELECT u.user_id, u.login, u.name, " +
            "u.email, u.birthday " +
            "FROM users AS u " +
            "WHERE user_id = ?";
    private static final String SQL_UPDATE_USER = "UPDATE users SET " +
            "login = ?, name = ?, email = ?, birthday = ?" +
            "WHERE user_id = ?";


    @Override
    public List<User> findAll() {
        log.info("UserDbStorage: Получен запрос на получение всех пользователей.");
        String sqlToAllUsers = "SELECT * " +
                "FROM users";
        List<User> listOfUsers = jdbcTemplate.query(sqlToAllUsers, CustomRowMapper::mapRowToUser);
        return listOfUsers;
    }

    @Override
    public User create(User user)  {
        log.info("UserDbStorage: Получен запрос на добавление пользователя {}.", user.getName());

        SimpleJdbcInsert filmJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        int id = filmJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(id);
        log.info("UserDbStorage: добавлен пользователь с id - {}.", id);
        return getUser(user.getId());
    }

    @Override
    public User update(User user) {
        log.info("UserDbStorage: Получен запрос на обновление пользователя  с id: {}.", user.getId());

        jdbcTemplate.update(SQL_UPDATE_USER,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        log.info("UserDbStorage: Пользователь с id: {} успешно обновлен.", user.getId());
        return getUser(user.getId());
    }

    @Override
    public User getUser(int userID)  {
        log.info("UserDbStorage: Получен запрос на получение пользователя с id: {}.", userID);
        User user = jdbcTemplate.queryForObject(SQL_GET_BY_ID, CustomRowMapper::mapRowToUser, userID);
        log.trace("UserDbStorage: Получен пользователь: {} {}", user.getId(), user.getName());


        String sqlToFriends = "SELECT friends_id " +
                "FROM friends_line " +
                "WHERE user_id = ?";

        Set<Integer> friends = new HashSet<>(jdbcTemplate.query(sqlToFriends, CustomRowMapper::mapRowToFriends, userID));
        user.setFriendsID(friends);

        return user;
    }

    @Override
    public boolean validateDataExists(int id) {
        int count = jdbcTemplate.queryForObject(SQL_VALIDATE, CustomRowMapper::mapRowCount, id);
        return count != 0;
    }
}
