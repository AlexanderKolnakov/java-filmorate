package ru.yandex.practicum.filmorate.storage;

public interface FriendStorage {
    void addFriend(int userID, int friendID);
    void deleteFriend(int userID, int friendID);
}
