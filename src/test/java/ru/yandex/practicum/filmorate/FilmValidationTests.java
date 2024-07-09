package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmValidationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createFilmWithNullName_shouldReturnBadRequest() {
        Film film = Film.builder()
                .name(null)
                .description("Amazing movie")
                .releaseDate(LocalDate.now().minusYears(5))
                .duration(130)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createFilmWithTooLongDescription_shouldReturnBadRequest() {
        String description = "Lorem ipsum".repeat(20);
        Film film = Film.builder().name("Epic Movie").description(description).
                releaseDate(LocalDate.now().minusYears(3)).duration(160).build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createFilmWithNegativeDuration_shouldReturnBadRequest() {
        Film film = Film.builder()
                .name("Negative Duration Film")
                .description("This film has a negative duration")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(-90)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void updateFilmWithNullName_shouldReturnBadRequest() {
        Film film = Film.builder()
                .name("Initial Film")
                .description("Initial description")
                .releaseDate(LocalDate.now().minusYears(8))
                .duration(120)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        Film filmToUpdate = Film.builder()
                .name(null)
                .description("Updated description")
                .releaseDate(LocalDate.now().minusYears(8))
                .duration(120)
                .build();
        HttpEntity<Film> entity = new HttpEntity<>(filmToUpdate);
        ResponseEntity<Film> updateResponse = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);

        assertEquals("400 BAD_REQUEST", updateResponse.getStatusCode().toString());
    }

    @Test
    void updateFilmWithTooLongDescription_shouldReturnBadRequest() {
        Film film = Film.builder()
                .name("Short Description Film")
                .description("Short description")
                .releaseDate(LocalDate.now().minusYears(6))
                .duration(100)
                .build();
        restTemplate.postForLocation("/films", film);

        String longDescription = "Lorem ipsum".repeat(20);
        Film filmToUpdate = Film.builder()
                .name("Updated Film")
                .description(longDescription)
                .releaseDate(LocalDate.now().minusYears(6))
                .duration(100)
                .build();
        HttpEntity<Film> entity = new HttpEntity<>(filmToUpdate);
        ResponseEntity<Film> updateResponse = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);

        assertEquals("400 BAD_REQUEST", updateResponse.getStatusCode().toString());
    }

    @Test
    void updateFilmWithNegativeDuration_shouldReturnBadRequest() {
        Film film = Film.builder()
                .name("Normal Duration Film")
                .description("This film has a normal duration")
                .releaseDate(LocalDate.now().minusYears(15))
                .duration(110)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        Film filmToUpdate = Film.builder()
                .name("Normal Duration Film")
                .description("This film has a normal duration")
                .releaseDate(LocalDate.now().minusYears(15))
                .duration(-110)
                .build();
        HttpEntity<Film> entity = new HttpEntity<>(filmToUpdate);
        ResponseEntity<Film> updateResponse = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);

        assertEquals("400 BAD_REQUEST", updateResponse.getStatusCode().toString());
    }
}
