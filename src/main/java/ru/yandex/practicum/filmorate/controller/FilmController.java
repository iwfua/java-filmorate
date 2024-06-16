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
    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private Long maxId = 0L;


    @GetMapping
    public List<Film> getFilms() {
        log.info("Фильмов в списке: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("Пришел POST запрос /films с телом: {}", newFilm);
        validateDateRelease(newFilm);

        newFilm.setId(nextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Отправлен ответ POST /films с телом: {}", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Пришел PUT запрос /films с телом: {}", film);
        validateDateRelease(film);
        validateId(film);
        Long filmId = film.getId();

        if (!films.containsKey(filmId)) {
            String errorMessage = String.format("Фильм с Id=%s не найден", filmId);
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
        films.put(film.getId(), film);
        log.info("Отправлен ответ PUT /users с телом: {}", film);
        return film;

    }

    private void validateId(Film film) {
        if (film.getId() == null) {
            String errorMessage = "Id не указан";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    private void validateDateRelease(Film film) {
        if (film.getReleaseDate().isBefore(DATE_RELEASE)) {
            String errorMessage = "Дата релиза указана неверно";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }


    private Long nextId() {
        return ++maxId;
    }
}
