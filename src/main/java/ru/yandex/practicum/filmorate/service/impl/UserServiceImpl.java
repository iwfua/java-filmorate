package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Component
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserStorage storage;

    @Autowired
    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = storage.getAllUsers();
        log.info("Запрошен список всех пользователей. Количество пользователей: {}", users.size());
        return users;
    }

    public User createUser(User user) {
        validate(user, "Форма пользователя заполнена неверно");
        preSave(user);
        User result = storage.createUser(user);
        log.info("Пользователь успешно добавлен: {}", user);
        return result;
    }

    public User updateUser(User user) {
        validate(user, "Форма обновления пользователя заполнена неверно");
        preSave(user);
        User result = storage.updateUser(user);
        log.info("Пользователь успешно обновлен: {}", user);
        return result;
    }

    public void deleteUser(Integer userId) {
        if (getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с ID = " + userId + " не найден");
        }
        storage.deleteUser(userId);
        log.info("Удален пользователь с id: {}", userId);
    }

    public User getUserById(Integer id) {
        User user = storage.getUserById(id);
        log.info("Запрошен пользователь с ID = {}. Найден пользователь: {}", id, user);
        return user;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        checkUser(userId, friendId);
        storage.addFriend(userId, friendId);
        log.info("Друг успешно добавлен для пользователя с id: {}", userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        checkUser(userId, friendId);
        storage.removeFriend(userId, friendId);
        log.info("Друг успешно удален для пользователя с id: {}", userId);
    }

    @Override
    public List<User> getFriends(Integer userId) {
        checkUser(userId, userId);
        List<User> result = storage.getFriends(userId);
        log.info("Друзья пользователя с ID = {}: {}", userId, result);
        return result;
    }

    @Override
    public List<User> getMutualFriends(Integer user1Id, Integer user2Id) {
        checkUser(user1Id, user2Id);
        List<User> result = storage.getCommonFriends(user1Id, user2Id);
        log.info("Общие друзья пользователей с ID {} и {}: {}", user1Id, user2Id, result);
        return result;
    }

    private void checkUser(Integer userId, Integer friendId) {
        storage.getUserById(userId);
        storage.getUserById(friendId);
    }

    private void validate(User user, String message) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }

    private void preSave(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
