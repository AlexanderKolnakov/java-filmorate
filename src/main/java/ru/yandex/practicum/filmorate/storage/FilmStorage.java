package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public List<Film> findAll();



    public Film create(Film film) throws ValidateException;



    public Film update(Film film) throws ValidateException, IDException;

}
