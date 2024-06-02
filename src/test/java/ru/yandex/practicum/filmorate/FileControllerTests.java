package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FileControllerTests {
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void createFilm() {
        LocalDate releaseDate = LocalDate.of(1895, 12, 28);
        Film film = new Film(1L, "name", "description", releaseDate, 1);

        filmController.createFilm(film);

        Assertions.assertEquals(filmController.getFilms().size(), 1);
        System.out.println(filmController.getFilms());
    }

    @Test
    public void createUserWithInvalidReleaseDate() {

        LocalDate invalidReleaseDate = LocalDate.of(1895, 12, 22);
        Film film = new Film(1L, "name", "Description", invalidReleaseDate, 1);

        ValidationException exception =
                Assertions.assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Assertions.assertEquals(exception.getMessage(), "Дата релиза указана неверно");
    }

}
