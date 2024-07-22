package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserValidationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createUserWithInvalidEmail_shouldReturnBadRequest() {
        User user = new User(1, "invalidemail.com", "user1", "John Doe", LocalDate.now().minusYears(30));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createUserWithEmptyLogin_shouldReturnBadRequest() {
        User user = new User(2, "user@example.com", " ", "Jane Doe", LocalDate.now().minusYears(25));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createUserWithFutureBirthdate_shouldReturnBadRequest() {
        User user = new User(3, "futureuser@example.com", "futureuser", "Future User", LocalDate.now().plusYears(10));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void updateUserToEmptyLogin_shouldReturnBadRequest() {
        User initialUser = new User(5, "updatelogin@example.com", "updatelogin", "Update Login", LocalDate.now().minusYears(22));
        restTemplate.postForEntity("/users", initialUser, User.class);

        User updatedUser = new User(5, "updatelogin@example.com", " ", "Update Login", LocalDate.now().minusYears(22));
        HttpEntity<User> entity = new HttpEntity<>(updatedUser);
        ResponseEntity<User> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void updateUserWithFutureBirthdate_shouldReturnBadRequest() {
        User initialUser = new User(6, "updatebirthdate@example.com", "updatebirthdate", "Update Birthdate", LocalDate.now().minusYears(40));
        restTemplate.postForEntity("/users", initialUser, User.class);

        User updatedUser = new User(6, "updatebirthdate@example.com", "updatebirthdate", "Update Birthdate", LocalDate.now().plusYears(5));
        HttpEntity<User> entity = new HttpEntity<>(updatedUser);
        ResponseEntity<User> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }
}
