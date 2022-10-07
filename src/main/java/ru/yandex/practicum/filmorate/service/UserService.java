package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    public final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(long userID, long friendID) throws IDException {
        final User user = inMemoryUserStorage.getUser(userID);
        final User friend = inMemoryUserStorage.getUser(friendID);
        user.getFriendsID().add(friendID);
        friend.getFriendsID().add(userID);
    }

    public void deleteFriend(long userID, long friendID) throws IDException {
        final User user = inMemoryUserStorage.getUser(userID);
        final User friend = inMemoryUserStorage.getUser(friendID);
        user.getFriendsID().remove(friendID);
        friend.getFriendsID().remove(userID);
    }

    public List<User> showGeneralFriends(long userID, long friendID) throws IDException {
        List<User> userFriends = showFriend(userID);
        List<User> friendFriends = showFriend(friendID);
        return userFriends.stream().filter(friendFriends::contains).collect(Collectors.toList());
    }
    public List<User> showFriend(long userID) throws IDException {
        final User user = inMemoryUserStorage.getUser(userID);
        List<User> userFriends = new ArrayList<>();
        for (long friends : user.getFriendsID()) {
            userFriends.add(inMemoryUserStorage.getUser(friends));
        }
        return userFriends;
    }
}
