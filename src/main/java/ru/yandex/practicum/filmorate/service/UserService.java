package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    public final UserStorage userStorage;
    public final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(int userID, int friendID) throws IDException {
        validateID(userID);
        validateID(friendID);
        friendStorage.addFriend(userID, friendID);
    }

    public void deleteFriend(int userID, int friendID) throws IDException {
        validateID(userID);
        validateID(friendID);
        friendStorage.deleteFriend(userID, friendID);
    }

    public List<User> showGeneralFriends(int userID, int friendID) throws IDException {
        List<User> userFriends = showFriend(userID);
        List<User> friendFriends = showFriend(friendID);
        return userFriends.stream().filter(friendFriends::contains).collect(Collectors.toList());
    }

    public List<User> showFriend(int userID) throws IDException {
        validateID(userID);
        final User user = userStorage.getUser(userID);
        List<User> userFriends = new ArrayList<>();
        for (int friends : user.getFriendsID()) {
            userFriends.add(userStorage.getUser(friends));
        }
        return userFriends;
    }

    public User create(User user) throws ValidateException {
        validateName(user);
        return userStorage.create(user);
    }

    public User getUser(int id) throws IDException {
        validateID(id);
        return userStorage.getUser(id);
    }
    public List<User> findAll() {
        return userStorage.findAll();
    }
    public User update(User user) throws IDException, ValidateException {
        validateID(user.getId());
        validateName(user);
        return userStorage.update(user);
    }

    public void validateID(Integer id) throws IDException {
        if (!userStorage.validateDataExists(id)) {
            throw new IDException("Пользователя с id: " + id + " не существует.");
        }
    }
    public void validateName(User user) throws ValidateException {
        if (user.getName()== null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        } else if (user.getName().isBlank()) {
            throw new ValidateException("Имя пользователя не может быть пустым, ему присвоено " +
                    "имя в соответствии с его логином: " + user.getName());
        }
    }
}
