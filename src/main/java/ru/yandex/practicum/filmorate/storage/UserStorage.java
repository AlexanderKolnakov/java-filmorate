package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> findAll();

    public User create(User user) throws ValidateException;

    public User update(User user) throws ValidateException, IDException;
    User getUser(int userID) throws IDException;

    public boolean validateDataExists(int id);
}
