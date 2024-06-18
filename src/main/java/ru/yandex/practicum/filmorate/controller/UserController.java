package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private final UserStorage userStorage;
    @Autowired
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        log.info("Пришел запрос GET /users");
        List<User> users = userStorage.findUsers();
        log.info("Отправлен ответ GET /users с телом: {}", users);
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User newUser) {
        log.info("Пришел POST запрос /users с телом: {}", newUser);
        userStorage.createUser(newUser);
        log.info("Отправлен ответ POST /users с телом: {}", newUser);
        return newUser;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Пришел PUT запрос /users с телом: {}", user);
        userStorage.updateUser(user);
        log.info("Отправлен ответ PUT /users с телом: {}", user);
        return user;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Пришел PUT запрос /users/{}/friends/{}", userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("Отправлен ответ PUT /users/{}/friends/{}", userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable Long userId) {
        log.info("Пришел запрос GET /users/{userId}/friends");
        List<User> userFiends = userService.getFriends(userId);
        log.info("Отправлен ответ GET /users/{userId}/friends с телом: {}", userFiends);
        return userFiends;
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Пришел запрос DELETE /users/{}/friends/{}", userId, friendId);
        userService.deleteFriend(userId, friendId);
        log.info("Отправлен ответ DELETE /users/{}/friends/{}", userId, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Пришел запрос GET /users/{}/friends/common/{}", id, otherId);
        List<User> mutualFriends = userService.getMutualFriends(id, otherId);
        log.info("Отправлен ответ GET /users/{}/friends/common/{} с телом: {}", id, otherId, mutualFriends);
        return mutualFriends;
    }
}
