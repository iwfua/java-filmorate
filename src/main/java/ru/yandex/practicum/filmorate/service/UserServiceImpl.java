package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserStorage userStorage;

    @Override
    public void addFriend(Long userId, Long friendId) {
        User userFriend = userStorage.getUserById(friendId);
        User user = userStorage.getUserById(userId);

        user.addFriend(friendId);
        userFriend.addFriend(userId);
        log.info("Пользователи userId={} и friendId={} стали друзьями", userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        List<User> userIdFriends = userStorage.getUserById(userId).getFriendsId().stream()
                .map(userStorage::getUserById)
                .toList();
        log.debug("У пользователя с id={} найдены друзья {}", userId, userIdFriends);
        return userIdFriends;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(friendId);

        user.deleteFriend(userFriend);
        userFriend.deleteFriend(user);
        log.info("Пользователь c id={}, удалил из друзей friendId={}", userId, friendId);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        Set<Long> userFriendIds = userStorage.getUserById(userId).getFriendsId();
        Set<Long> otherUserFriendIds = userStorage.getUserById(otherUserId).getFriendsId();

        userFriendIds.retainAll(otherUserFriendIds);
        log.debug("Id общих друзей пользователя id={} и id={}: {}", userId, otherUserId, userFriendIds);
        return userFriendIds.stream().map(userStorage::getUserById).toList();
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User createUser(User user) {
        setNameIfAbsent(user);
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        setNameIfAbsent(user);
        return userStorage.updateUser(user);
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

        if (!userStorage.getUserById(user.getId()).getId().equals(user.getId())) {
            String errorMessage = "User с id=" + user.getId() + " не найден";
            User user1 = userStorage.getUserById(user.getId());
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }
}
