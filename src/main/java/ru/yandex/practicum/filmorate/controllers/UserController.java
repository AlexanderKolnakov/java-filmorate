package ru.yandex.practicum.filmorate.controllers;


import ru.yandex.practicum.filmorate.exceptions.UserException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.UserIDException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    private int usersID = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws UserException {
        log.info("Получен POST запрос /users. Передано: {}", user);
        user.setId(usersID);
        checkUser(user);
        usersID++;
        users.put(user.getId(), user);
        log.info("Пользователю: {} присвоен ID: {}", user.getName(), user.getId());
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) throws UserException, UserIDException {
        log.info("Получен PUT запрос /users. Передано: {}", user);
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователя с переданным id: {} не существует.", user.getId());
            throw new UserIDException("Пользователя с id: " + user.getId() + " не существует.");
        } else {
            checkUser(user);
            users.put(user.getId(), user);
            log.info("Информация о пользователе: {} с ID: {} успешно обновлена", user.getName(), user.getId());
        }
        return user;
    }

    private void checkUser(User user) throws UserException {
        if (user.getName()== null) {
            user.setName(user.getLogin());
        } else if (user.getName().isBlank()) {
            log.warn("Имя пользователя не может быть пустым, ему присвоено имя в соответствии с его логином: {}", user.getName());
            throw new UserException("Имя пользователя не может быть пустым, ему присвоено имя в соответствии с его логином: "
                    + user.getName());
        }
    }
}


