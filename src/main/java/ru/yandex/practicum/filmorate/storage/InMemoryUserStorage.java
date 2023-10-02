package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private int usersID = 1;
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) throws ValidateException {
        user.setId(usersID++);
        checkUser(user);
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) throws ValidateException, IDException {
        if (!users.containsKey(user.getId())) {
            throw new IDException("Пользователя с id: " + user.getId() + " не существует.");
        } else {
            checkUser(user);
            users.put(user.getId(), user);
        }
        return user;
    }

    private void checkUser(User user) throws ValidateException {
        if (user.getName()== null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        } else if (user.getName().isBlank()) {
            throw new ValidateException("Имя пользователя не может быть пустым, ему присвоено имя в соответствии с его логином: "
                    + user.getName());
        }
    }

    public User getUser(int userID) throws IDException {
        if (!users.containsKey(userID)) {
            throw new IDException("Пользователя с id: " + userID + " не существует.");
        }
        return users.get(userID);
    }

    @Override
    public boolean validateDataExists(int id) {
        return false;
    }
}
