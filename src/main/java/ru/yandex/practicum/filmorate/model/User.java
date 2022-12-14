package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    long id;

    @Email(message = "Не корректный адрес электронной почты")
    final String email;

    @NotNull(message = "Логин не может быть null")
    @NotBlank(message = "Логин не может быть пустым")
    final String login;

    String name;

    @Past(message = "Дата рождения не может быть в будущем")
    final LocalDate birthday;

    final Set<Long> friendsID = new HashSet<>();
    }


