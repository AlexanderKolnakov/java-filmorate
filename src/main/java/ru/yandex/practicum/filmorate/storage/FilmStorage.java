package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();
    Film create(Film film) throws ValidateException;
    Film update(Film film) throws ValidateException, IDException;
    Film getFilm(int filmsID) throws IDException;
    boolean validateDataExists(int id);
}
