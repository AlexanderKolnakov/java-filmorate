package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.exceptions.FilmIDException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public List<Film> findAll();



    public Film create(Film film) throws FilmException ;



    public Film update(Film film) throws FilmException, FilmIDException ;

}
