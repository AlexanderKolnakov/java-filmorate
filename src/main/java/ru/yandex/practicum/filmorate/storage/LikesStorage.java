package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    void addLike(int userID, int filmID);
    void deleteLike(int userID, int filmID);
}
