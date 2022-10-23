package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;


    @Autowired
  public FilmService(@Qualifier("FilmDbStorage")FilmStorage filmStorage, @Qualifier("UserDbStorage")UserStorage userStorage) {
            this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public void addLike(int userID, int filmID) throws IDException {
        final User user = userStorage.getUser(userID);
        final Film film = filmStorage.getFilm(filmID);
        film.addLike(user.getId());
    }

    public void deleteLike(int userID, int filmID) throws IDException {
        final User user = userStorage.getUser(userID);
        final Film film = filmStorage.getFilm(filmID);
        film.deleteLike(user.getId());
    }


    public List<Film> getMostLikedFilms(int sizeOfList) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingLong(Film::getLikes).reversed())
                .limit(sizeOfList)
                .collect(Collectors.toList());
    }
}
