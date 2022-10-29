package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Slf4j
public class Film {

    private int id;

    @NotNull(message = "Имя не может быть null")
    @NotBlank(message = "Имя не может быть пустым")
    private final String name;

    @Size(max = 200, message = "Описание фильма должно содержать не более 200 символов")
    @NotNull(message = "Описание фильма не может быть null")
    private final String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным целочисленным числом.")
    private final int duration;

    @NotNull(message = "Рейтинг не может быть null")
    private final int rate;

    @NotNull(message = "Рейтинг (Mpa) не может быть null")
    private final Mpa mpa;

    private Set<Genre> genres = new TreeSet<>();

    private Set<Integer> usersLikes = new HashSet<>();

//    private int likes = 0;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, int rate, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
    }

    public void setLikes(Set<Integer> likesSet) {
        usersLikes = likesSet;
//        likes = usersLikes.size();
    }

    public Map<String,Object> toMap() {

        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rate", rate);
        values.put("mpa_id", mpa.getId());
        return values;
    }

    public void setGenres(Set<Genre> genresSet) {
        genres = genresSet;
    }

    public int getFilmLikes() {
        return usersLikes.size();
    }
}
