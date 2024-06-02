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
    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        log.info("Пользователей в списке: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        setNameIfAbsent(newUser);

        newUser.setId(nextId());
        users.put(newUser.getId(), newUser);
        log.info("User с id={} добавлен в список", newUser.getId());
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            String errorMessage = "id не указан";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (users.containsKey(user.getId())) {
            setNameIfAbsent(user);
            users.put(user.getId(), user);
            log.info("User с id={} обновлен", user.getId());
            return user;
        }

        String errorMessage = "User с id=" + user.getId() + "не найден";
        log.warn(errorMessage);
        throw new ValidationException(errorMessage);
    }

    private static void setNameIfAbsent(User user) {
        if (user.getName() == null) {
            log.info("В поле name используется login");
            user.setName(user.getLogin());
        }
    }

    private Long nextId() {
        long maxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++maxId;
    }
}
