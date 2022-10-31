package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
        public List<Film> findAllFilms() {
        log.info("Получен GET запрос /films.");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable int id) throws IDException {
        log.info("Получен GET запрос /films/{}.", id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> findMostLikedFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен GET запрос /films/popular?count={}.", count);
        List<Film> listOfFilms = filmService.getMostLikedFilms(count);
        log.info("Список наиболее популярных фильмов: {}", listOfFilms);
        return listOfFilms;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidateException {
        log.info("Получен POST запрос /films. Передано: {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidateException, IDException {
        log.info("Получен PUT запрос /films. Передано: {}", film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) throws IDException {
        log.info("Получен PUT запрос /films/{}/like/{}.", id, userId);
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) throws IDException {
        log.info("Получен DELETE запрос /films/{}/like/{}.", id, userId);
        filmService.deleteLike(userId, id);
    }
}
