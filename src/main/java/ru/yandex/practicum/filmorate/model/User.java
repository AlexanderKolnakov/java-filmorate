package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {
    private int id;
    @NotNull(message = "Логин не может быть null")
    @NotBlank(message = "Логин не может быть пустым")
    private final String login;

    String name;

    @Email(message = "Не корректный адрес электронной почты")
    private final String email;

    @Past(message = "Дата рождения не может быть в будущем")
    private final LocalDate birthday;

    private Set<Integer> friendsID = new HashSet<>();

    public Map<String,Object> toMap() {

        Map<String, Object> values = new HashMap<>();
        values.put("login", login);
        values.put("name", name);
        values.put("email", email);
        values.put("birthday", birthday);
        return values;
    }

    public User(int id, String login, String name, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}


