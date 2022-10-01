package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmIDException;
import ru.yandex.practicum.filmorate.exceptions.UserIDException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public final InMemoryFilmStorage inMemoryFilmStorage;
    public final InMemoryUserStorage inMemoryUserStorage;


    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    public void addLike(long userID, long filmID) throws FilmIDException, UserIDException {
        final User user = inMemoryUserStorage.getUser(userID);
        final Film film = inMemoryFilmStorage.getFilm(filmID);
        film.addLike(user.getId());
    }

    public void deleteLike(long userID, long filmID) throws FilmIDException, UserIDException {
        final User user = inMemoryUserStorage.getUser(userID);
        final Film film = inMemoryFilmStorage.getFilm(filmID);
        film.deleteLike(user.getId());
    }


    public List<Film> getMostLikedFilms(long sizeOfList) {
        return inMemoryFilmStorage.findAll().stream()
                .sorted(Comparator.comparingLong(Film::getLikes).reversed())
                .limit(sizeOfList)
                .collect(Collectors.toList());
    }
}
