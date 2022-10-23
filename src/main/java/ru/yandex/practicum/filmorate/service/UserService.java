package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    public final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage")UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userID, int friendID) throws IDException {
        final User user = userStorage.getUser(userID);
        final User friend = userStorage.getUser(friendID);
        user.getFriendsID().add(friendID);
        friend.getFriendsID().add(userID);
    }

    public void deleteFriend(int userID, int friendID) throws IDException {
        final User user = userStorage.getUser(userID);
        final User friend = userStorage.getUser(friendID);
        user.getFriendsID().remove(friendID);
        friend.getFriendsID().remove(userID);
    }

    public List<User> showGeneralFriends(int userID, int friendID) throws IDException {
        List<User> userFriends = showFriend(userID);
        List<User> friendFriends = showFriend(friendID);
        return userFriends.stream().filter(friendFriends::contains).collect(Collectors.toList());
    }
    public List<User> showFriend(int userID) throws IDException {
        final User user = userStorage.getUser(userID);
        List<User> userFriends = new ArrayList<>();
        for (int friends : user.getFriendsID()) {
            userFriends.add(userStorage.getUser(friends));
        }
        return userFriends;
    }
}
