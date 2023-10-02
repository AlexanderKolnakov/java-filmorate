package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

@Component
@AllArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    @Override
    public void addFriend(int userID, int friendID) {
        log.info("FriendDbStorage: Пользователь с id: {} добавляет пользователя с id {} в друзья.", userID, friendID);
        String sqlAddFriend = "INSERT INTO friends_line(user_id, friends_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlAddFriend, userID, friendID);
    }

    @Override
    public void deleteFriend(int userID, int friendID) {
        log.info("FriendDbStorage: У пользователь с id: {} удаляется друг с id {}.", userID, friendID);
        String sqlDeleteFriend = "DELETE FROM friends_line WHERE user_id = ? AND friends_id = ?";
        log.info("FriendDbStorage: Удаление дружбы прошло успешно");
        jdbcTemplate.update(sqlDeleteFriend, userID, friendID);
    }
}
