package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public List<Mpa> findAllMpa() {
        log.info("Получен GET запрос /mpa.");
        return mpaService.findAll();
    }

    @GetMapping("/mpa/{id}")
    public Mpa findMpa(@PathVariable int id) throws IDException {
        log.info("Получен GET запрос /mpa/{}.", id);
        return mpaService.getMpa(id);
    }
}
