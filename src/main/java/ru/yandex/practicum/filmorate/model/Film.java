package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    long id;

    @NotNull(message = "Имя не может быть null")
    @NotBlank(message = "Имя не может быть пустым")
    final String name;

    @Size(max = 200, message = "Описание фильма должно содержать не более 200 символов")
    final String description;

    final LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным целочисленным числом.")
    final long duration;

    private Set<Long> usersLikes = new HashSet<>();

    private long likes = 0;


    public void addLike(long userID) {
        usersLikes.add(userID);
        likes = usersLikes.size();
    }

    public void deleteLike(long userID) {
        usersLikes.remove(userID);
        likes = usersLikes.size();
    }


}
