package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User newUser);
    User updateUser(User user);
    User getUserById(Long id);
    List<User> findUsers();
}
