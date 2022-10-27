package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorege;

import java.util.List;

@Service
public class GenreService {
    public final GenreStorege genreStorege;

    @Autowired
    public GenreService(GenreStorege genreStorege) {
        this.genreStorege = genreStorege;
    }

    public List<Genre> findAll() {
        return genreStorege.findAll();
    }

    public  Genre getGenre(int id) throws IDException {
        validateID(id);
        return genreStorege.getGenre(id);
    }

    public void validateID(Integer id) throws IDException {
        if (!genreStorege.validateDataExists(id)) {
            throw new IDException("Genre с id: " + id + " не существует.");
        }
    }
}
