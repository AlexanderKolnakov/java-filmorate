package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;
    public final LikesStorage likesStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("UserDbStorage") UserStorage userStorage,
                       LikesStorage likesStorage) {
        this.likesStorage = likesStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilm(int id) throws IDException {
        validate(id);
        return filmStorage.getFilm(id);
    }

    public Film create(Film film) throws ValidateException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws IDException, ValidateException {
        validate(film.getId());
        return filmStorage.update(film);
    }

    public List<Film> getMostLikedFilms(int sizeOfList) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingLong(Film::getLikes).reversed())
                .limit(sizeOfList)
                .collect(Collectors.toList());
    }

    public void addLike(int userID, int filmID) throws IDException {
        validate(filmID);
        validateUser(userID);
        likesStorage.addLike(userID, filmID);
    }

    public void deleteLike(int userID, int filmID) throws IDException {
        validate(filmID);
        validateUser(userID);
        likesStorage.deleteLike(userID, filmID);
    }

    public void validate(Integer id) throws IDException {
        if (!filmStorage.validateDataExists(id)) {
            throw new IDException("Фильма с id: " + id + " не существует.");
        }
    }

    public void validateUser(Integer id) throws IDException {
        if (!userStorage.validateDataExists(id)) {
            throw new IDException("Пользователя с id: " + id + " не существует.");
        }
    }
}
