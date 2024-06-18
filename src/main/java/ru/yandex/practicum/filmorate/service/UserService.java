package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void addFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    void deleteFriend(Long userId,Long friendId);

    List<User> getMutualFriends(Long userId, Long otherUserId);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);
}
