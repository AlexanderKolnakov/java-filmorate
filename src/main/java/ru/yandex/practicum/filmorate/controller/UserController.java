package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.UserIDException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    public final InMemoryUserStorage inMemoryUserStorage;
    public final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Получен GET запрос /users.");
        return inMemoryUserStorage.findAll();
    }
    @GetMapping("/users/{id}")
    public User findUser(@PathVariable long id) throws UserIDException {
        log.info("Получен GET запрос /users/{}.", id);
        return inMemoryUserStorage.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriends(@PathVariable long id) throws UserIDException {
        log.info("Получен GET запрос /users/{}/friends.", id);
        return userService.showFriend(id);

    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findGeneralFriends(@PathVariable long id, @PathVariable long otherId) throws UserIDException {
        log.info("Получен GET запрос /users/{}/friends/common/{}", id, otherId);
        return userService.showGeneralFriends(id, otherId);
    }

    @PostMapping( "/users")
    public User create(@Valid @RequestBody User user) throws UserException {
        log.info("Получен POST запрос /users. Передано: {}", user);
        inMemoryUserStorage.create(user);
        log.info("Пользователю: {} присвоен ID: {}", user.getName(), user.getId());
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) throws UserException, UserIDException {
        log.info("Получен PUT запрос /users. Передано: {}", user);
        inMemoryUserStorage.update(user);
        return user;
    }
    @PutMapping("/users/{id}/friends/{friendId}")
    public void putLikeForFilm (@PathVariable long id, @PathVariable long friendId) throws UserIDException {
        log.info("Получен PUT запрос /users/{}/friends/{}.", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteLikeForFilm (@PathVariable long id, @PathVariable long friendId) throws UserIDException {
        log.info("Получен DELETE запрос /users/{}/friends/{}.", id, friendId);
        userService.deleteFriend(id, friendId);
    }
}


