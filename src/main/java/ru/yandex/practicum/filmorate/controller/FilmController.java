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
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping("/films")
        public List<Film> findAll() {
        log.info("Получен GET запрос /films.");
        return filmService.findAll();
    }
    @GetMapping("/films/{id}")
    public Film findFilm(@PathVariable int id) throws IDException {
        log.info("Получен GET запрос /films/{}.", id);
        return filmService.getFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> findMostLikedFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен GET запрос /films/popular?count={}.", count);
        List<Film> listOfFilms = filmService.getMostLikedFilms(count);
        log.info("Список наиболее популярных фильмов: {}", listOfFilms);
        return listOfFilms;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) throws ValidateException {
        log.info("Получен POST запрос /films. Передано: {}", film);
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) throws ValidateException, IDException {
        log.info("Получен PUT запрос /films. Передано: {}", film);
        return filmService.update(film);
    }

//    @PutMapping("/films/{id}/like/{userId}")
//    public void addLike(@PathVariable int id, @PathVariable int userId) throws IDException {
//        log.info("Получен PUT запрос /films/{}/like/{}.", id, userId);
//        filmService.addLike(userId, id);
//    }
//
//    @DeleteMapping("/films/{id}/like/{userId}")
//    public void deleteLike(@PathVariable int id, @PathVariable int userId) throws IDException {
//        log.info("Получен DELETE запрос /films/{}/like/{}.", id, userId);
//        filmService.deleteLike(userId, id);
//    }

}
