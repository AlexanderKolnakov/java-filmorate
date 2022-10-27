package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface GenreStorege {
    List<Genre> findAll();

    Genre getGenre(int id);

    boolean validateDataExists(Integer id);
}
