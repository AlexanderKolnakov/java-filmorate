package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.exceptions.IDException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Получен GET запрос /users.");
        return userService.findAll();
    }
    @GetMapping("/users/{id}")
    public User findUser(@PathVariable int id) throws IDException {
        log.info("Получен GET запрос /users/{}.", id);
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriends(@PathVariable int id) throws IDException {
        log.info("Получен GET запрос /users/{}/friends.", id);
        return userService.showFriend(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findGeneralFriends(@PathVariable int id, @PathVariable int otherId) throws IDException {
        log.info("Получен GET запрос /users/{}/friends/common/{}", id, otherId);
        return userService.showGeneralFriends(id, otherId);
    }
//
    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) throws ValidateException {
        log.info("Получен POST запрос /users. Передано: {}", user);
        userService.create(user);
        log.info("Пользователю: {} присвоен ID: {}", user.getName(), user.getId());
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) throws ValidateException, IDException {
        log.info("Получен PUT запрос /users. Передано: {}", user);
        userService.update(user);
        return user;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void putLikeForFilm (@PathVariable int id, @PathVariable int friendId) throws IDException {
        log.info("Получен PUT запрос /users/{}/friends/{}.", id, friendId);
        userService.addFriend(id, friendId);
    }

//    @DeleteMapping("/users/{id}/friends/{friendId}")
//    public void deleteLikeForFilm (@PathVariable int id, @PathVariable int friendId) throws IDException {
//        log.info("Получен DELETE запрос /users/{}/friends/{}.", id, friendId);
//        userService.deleteFriend(id, friendId);
//    }
}


