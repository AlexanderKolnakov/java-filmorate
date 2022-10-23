package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;


@Component
@Qualifier("UserDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User create(User user) throws ValidateException {
        return null;
    }

    @Override
    public User update(User user) throws ValidateException, IDException {
        return null;
    }

    @Override
    public User getUser(int userID) throws IDException {
        return null;
    }
}
