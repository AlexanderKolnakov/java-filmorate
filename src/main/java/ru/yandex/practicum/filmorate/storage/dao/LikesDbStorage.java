package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Component
@AllArgsConstructor
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    @Override
    public void addLike(int userID, int filmID) {
        log.info("LikesDbStorage: Пользователь с id: {} ставит like фильму с id {}.", userID, filmID);
        String sqlAddFriend = "INSERT INTO users_likes_film(film_id, user_id) VALUES (?, ?)";
        log.info("LikesDbStorage: Like успешно поставлен.");
        jdbcTemplate.update(sqlAddFriend, filmID, userID);
    }

    @Override
    public void deleteLike(int userID, int filmID) {
        log.info("LikesDbStorage: Пользователь с id: {} удаляется лайк у фильма с id {}.", userID, filmID);
        String sqlDeleteLike = "DELETE FROM users_likes_film WHERE film_id = ? AND user_id = ?";
        log.info("FriendDbStorage: Удаление дружбы прошло успешно");
        jdbcTemplate.update(sqlDeleteLike, filmID, userID);
    }
}
