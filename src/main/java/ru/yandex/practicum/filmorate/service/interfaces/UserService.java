package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void addFriend(Integer userId, Integer friendId);

    List<User> getFriends(Integer userId);

    void deleteFriend(Integer userId,Integer friendId);

    List<User> getMutualFriends(Integer userId, Integer otherUserId);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);
}
