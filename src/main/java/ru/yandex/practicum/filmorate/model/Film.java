package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {

    int id;
    @NotNull(message = "Имя не может быть null")
    @NotBlank(message = "Имя не может быть пустым")
    final String name;
    @Size(max = 200, message = "Описание фильма должно содержать не более 200 символов")
    final String description;
    final LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным целочисленным числом.")
    final long duration;

}
