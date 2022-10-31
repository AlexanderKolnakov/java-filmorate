package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();
    User create(User user) throws ValidateException;
    User update(User user) throws ValidateException, IDException;
    User getUser(int userID) throws IDException;
    boolean validateDataExists(int id);
}
