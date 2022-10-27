package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    public final GenreStorage genreStorage;
    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }
    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public  Genre getGenre(int id) throws IDException {
        validateID(id);
        return genreStorage.getGenre(id);
    }

    public void validateID(Integer id) throws IDException {
        if (!genreStorage.validateDataExists(id)) {
            throw new IDException("Genre с id: " + id + " не существует.");
        }
    }
}
