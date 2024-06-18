package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
@AllArgsConstructor
public class User {
    @PositiveOrZero
    private Long id;
    @Email
    private String email;
    @NotNull @NotBlank
    private String login;
    private String name;
    @NotNull @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private final Set<Long> friendsId = new HashSet<>();

    public void addFriend(Long friendId) {
        log.info("Пользователь с id {} добавлен в друзья", friendId);
        friendsId.add(friendId);
    }

    public void deleteFriend(User user) {
        log.info("Пользователь с id {} удален из друзей", user.getId());
        friendsId.remove(user.getId());
    }
}
