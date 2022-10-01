package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.exceptions.UserIDException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> findAll();

    public User create(User user) throws UserException;

    public User update(User user) throws UserException, UserIDException;
}
