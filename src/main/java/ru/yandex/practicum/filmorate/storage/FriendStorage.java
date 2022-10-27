package ru.yandex.practicum.filmorate.storage;

public interface FriendStorage {
    public void addFriend(int userID, int friendID);

    public void deleteFriend(int userID, int friendID);
}
