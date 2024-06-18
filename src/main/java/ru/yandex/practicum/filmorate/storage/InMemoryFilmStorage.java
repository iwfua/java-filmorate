package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Service
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long maxId = 0;

    @Override
    public Film createFilm(Film newFilm) {
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
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        return films.get(filmId);
    }

    private long nextId() {
        return ++maxId;
    }
}
