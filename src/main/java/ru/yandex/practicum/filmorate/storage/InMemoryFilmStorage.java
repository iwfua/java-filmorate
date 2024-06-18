package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private long maxId = 0;

    @Override
    public Film createFilm(Film newFilm) {
        validateDateRelease(newFilm);

        newFilm.setId(nextId());
        films.put(newFilm.getId(), newFilm);
        log.debug("Создан фильм: {}", newFilm);
        return newFilm;
    }

    @Override
    public Film deleteFilm(Film film) {
        Optional<Long> filmId = Optional.ofNullable(film.getId());
        if (filmId.isEmpty()) {
            throw new ValidationException("Id не указан");
        }
        films.remove(filmId.get());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateDateRelease(film);
        validateId(film);
        Long filmId = film.getId();

        if (!films.containsKey(filmId)) {
            String errorMessage = String.format("Фильм с Id=%s не найден", filmId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        return films.get(filmId);
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

    private long nextId() {
        return ++maxId;
    }
}
