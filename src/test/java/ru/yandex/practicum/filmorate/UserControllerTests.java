package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTests {
    UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void testCreateUser() {
        User user = new User(1L, "email@yandex.ru", "login", "name",
                LocalDate.now().minusMonths(1));
        userController.createUser(user);

        Assertions.assertEquals(userController.getUsers().size(),1);
    }

    @Test
    public void testCreateUserWithEmptyName() {
        User user = new User(1L, "emailyandex.ru", "login", null,
                LocalDate.now().minusMonths(1));
        userController.createUser(user);

        Assertions.assertEquals(userController.getUsers().get(0).getName(), "login");
    }

}
