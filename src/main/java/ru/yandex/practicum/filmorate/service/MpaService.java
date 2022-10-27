package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    public final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public Mpa getMpa(int id) throws IDException {
        validateID(id);
        return  mpaStorage.getMpa(id);

    }

    public void validateID(Integer id) throws IDException {
        if (!mpaStorage.validateDataExists(id)) {
            throw new IDException("Mpa с id: " + id + " не существует.");
        }
    }
}
