package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.exceptions.FilmIDException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long filmsID = 1;
    private final Map<Long, Film> films = new HashMap<>();
    private final static LocalDate FILMS_BORN = LocalDate.of(1895, 12, 28);


    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }


    public Film create(Film film) throws FilmException {
        checkFilm(film);
        film.setId(filmsID++);
        films.put(film.getId(), film);
        return film;
    }


    public Film update(Film film) throws FilmException, FilmIDException {
        if (films.containsKey(film.getId())) {
            checkFilm(film);
            films.put(film.getId(), film);
        } else {
            throw new FilmIDException("Фильма с таким id: " + film.getId() + " не существует.");
        }
        return film;
    }

    private void checkFilm(Film film) throws FilmException {
        if (film.getReleaseDate().isBefore(FILMS_BORN)) {
            throw new FilmException("Дата выхода фильма не может быть раньше, чем: " + FILMS_BORN);
        }
    }
    public Film getFilm(long filmsID) throws FilmIDException {
        if (!films.containsKey(filmsID)) {
            throw new FilmIDException("Фильма с таким id: " + filmsID + " не существует.");
        }
        return films.get(filmsID);
    }
}
