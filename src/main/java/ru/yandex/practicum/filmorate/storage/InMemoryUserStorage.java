package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.exceptions.UserIDException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private long usersID = 1;
    private final Map<Long, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) throws UserException {
        user.setId(usersID++);
        checkUser(user);
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) throws UserException, UserIDException {
        if (!users.containsKey(user.getId())) {
            throw new UserIDException("Пользователя с id: " + user.getId() + " не существует.");
        } else {
            checkUser(user);
            users.put(user.getId(), user);
        }
        return user;
    }

    private void checkUser(User user) throws UserException {
        if (user.getName()== null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        } else if (user.getName().isBlank()) {
            throw new UserException("Имя пользователя не может быть пустым, ему присвоено имя в соответствии с его логином: "
                    + user.getName());
        }
    }

    public User getUser(long userID) throws UserIDException {
        if (!users.containsKey(userID)) {
            throw new UserIDException("Пользователя с id: " + userID + " не существует.");
        }
        return users.get(userID);
    }
}
