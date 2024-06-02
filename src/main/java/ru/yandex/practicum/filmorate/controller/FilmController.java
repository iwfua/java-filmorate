package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();
    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getFilms() {
        log.info("Фильмов в списке: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        checkReleaseDate(newFilm);

        newFilm.setId(nextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм с Id={} добавлен в список", newFilm.getId());
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            String errorMessage = "Id не указан";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        checkReleaseDate(film);

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Film с id={} успешно обновлён", film.getId());
            return film;
        }

        String errorMessage = String.format("Фильм с Id=%s не найден", film.getId());
        log.warn(errorMessage);
        throw new ValidationException(errorMessage);
    }

    private static void checkReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(DATE_RELEASE)) {
            String errorMessage = "Дата релиза указана неверно";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    private Long nextId() {
        long maxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++maxId;
    }

}
