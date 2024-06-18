package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id;

    @Override
    public User createUser(User newUser) {
        setNameIfAbsent(newUser);

        newUser.setId(hasNextId());
        users.put(newUser.getId(), newUser);
        log.debug("Создан пользователь: {}", newUser);
        return newUser;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        setNameIfAbsent(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователя не существует");
    }

    @Override
    public List<User> findUsers() {
        log.debug("Получение всех пользователей");
        return new ArrayList<>(users.values());
    }

    private void setNameIfAbsent(User user) {
        if (user.getName() == null) {
            log.debug("В поле name используется login");
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
            throw new NotFoundException(errorMessage);
        }
    }

    private Long hasNextId() {
        return ++id;
    }
}
