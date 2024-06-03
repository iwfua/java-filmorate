package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long maxId;

    @GetMapping
    public List<User> getUsers() {
        log.info("Отправлен ответ GET /users с телом: {}",users.values());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        log.info("Пришел POST запрос /users с телом: {}", newUser);
        setNameIfAbsent(newUser);

        newUser.setId(nextId());
        users.put(newUser.getId(), newUser);
        log.info("Отправлен ответ POST /users с телом: {}", newUser);
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Пришел PUT запрос /users с телом: {}", user);
        validate(user);
        setNameIfAbsent(user);
        users.put(user.getId(), user);
        log.info("Отправлен ответ PUT /users с телом: {}", user);
        return user;
    }

    private void setNameIfAbsent(User user) {
        if (user.getName() == null) {
            log.info("В поле name используется login");
            user.setName(user.getLogin());
        }
    }

    private void validate(User user) {
        if (user.getId() == null) {
            String errorMessage = "id не указан";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (!users.containsKey(user.getId())) {
            String errorMessage = "User с id=" + user.getId() + " не найден";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    private Long nextId() {
        return ++maxId;
    }
}
