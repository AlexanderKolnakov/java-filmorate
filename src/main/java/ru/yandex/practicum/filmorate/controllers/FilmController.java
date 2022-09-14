package ru.yandex.practicum.filmorate.controllers;


import ru.yandex.practicum.filmorate.exceptions.FilmException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.FilmIDException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private int filmsID = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private final static LocalDate FILMS_BORN = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws FilmException {
        log.info("Получен POST запрос /films. Передано: {}", film);
        film.setId(filmsID);
        checkFilm(film);
        filmsID++;
       films.put(film.getId(), film);
       log.info("Фильму: {} присвоен ID: {}", film.getName(), film.getId());
       return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) throws FilmException, FilmIDException {
        log.info("Получен PUT запрос /films. Передано: {}", film);
        if (films.containsKey(film.getId())) {
            checkFilm(film);
            films.put(film.getId(), film);
            log.info("Фильм с id: {} успешно обновлен", film.getId());
        } else {
            log.warn("Фильма с id: {} не существует.", film.getId());
            throw new FilmIDException("Фильма с таким id: " + film.getId() + " не существует.");
        }
        return film;
    }

    private void checkFilm(Film film) throws FilmException {
        if (film.getReleaseDate().isBefore(FILMS_BORN)) {
            log.warn("Дата выхода фильма {} не может быть раньше, чем: {}", film.getReleaseDate() ,FILMS_BORN);
            throw new FilmException("Дата выхода фильма не может быть раньше, чем: " + FILMS_BORN);
        }
    }
}
