package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends FriendsStorage {
    User createUser(User newUser);

    User updateUser(User user);

    User getUserById(Integer id);

    String deleteUser(int id);

    List<User> getAllUsers();
}
