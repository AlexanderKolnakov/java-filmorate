package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private int id;
    private final String name;

    public Genre(int genreId, String genreName) {
        this.id = genreId;
        this.name = genreName;
    }
}
