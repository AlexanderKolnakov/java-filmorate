package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    public final InMemoryFilmStorage inMemoryFilmStorage;
    public final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
        public List<Film> findAll() {
        log.info("Получен GET запрос /films.");
        return inMemoryFilmStorage.findAll();
    }
    @GetMapping("/films/{id}")
    public Film findFilm(@PathVariable long id) throws IDException {
        log.info("Получен GET запрос /films/{}.", id);
        return inMemoryFilmStorage.getFilm(id);
    }
    @GetMapping("/films/popular")
    public List<Film> findMostLikedFilms(@RequestParam(defaultValue = "10") long count) {
        log.info("Получен GET запрос /films/popular?count={}.", count);
        List<Film>  ff = filmService.getMostLikedFilms(count);
        log.info("{}", ff);
        return ff;
    }


    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) throws ValidateException {
        log.info("Получен POST запрос /films. Передано: {}", film);
        inMemoryFilmStorage.create(film);
        log.info("Фильму: {} присвоен ID: {}", film.getName(), film.getId());
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) throws ValidateException, IDException {
        log.info("Получен PUT запрос /films. Передано: {}", film);
        inMemoryFilmStorage.update(film);
        return film;
    }
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) throws IDException {
        log.info("Получен PUT запрос /films/{}/like/{}.", id, userId);
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) throws IDException {
        log.info("Получен DELETE запрос /films/{}/like/{}.", id, userId);
        filmService.deleteLike(userId, id);
    }

}
