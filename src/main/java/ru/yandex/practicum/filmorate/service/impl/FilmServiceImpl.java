package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public List<Film> getTopFilms(int count) {
        List<Film> result = new ArrayList<>(filmStorage.getPopular(count));
        log.info("Запрошен список популярных фильмов. Количество фильмов: {}", result.size());
        return result;
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (userStorage.getUserById(userId) != null) {
                filmStorage.addLike(filmId, userId);
                log.info("Лайк успешно добавлен");
            } else {
                throw new NotFoundException("Пользователь с ID = " + userId + " не найден");
            }
        } else {
            throw new NotFoundException("Фильм с ID = " + filmId + " не найден");
        }
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (userStorage.getUserById(userId) != null) {
                filmStorage.removeLike(filmId, userId);
                log.info("Лайк успешно удален");
            } else {
                throw new NotFoundException("Пользователь с ID = " + userId + " не найден");
            }
        } else {
            throw new NotFoundException("Фильм с ID = " + filmId + " не найден");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        log.info("Запрошен список всех фильмов. Количество фильмов: {}", films.size());
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        validateDateRelease(film);
        Film createdFilm = filmStorage.createFilm(film);
        log.info("Создан фильм: {}", createdFilm);
        return createdFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        validateId(film);
        validateDateRelease(film);
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Обновлен фильм: {}", updatedFilm);
        return updatedFilm;
    }

    @Override
    public void deleteFilmById(int filmId) {
        if (getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с ID = " + filmId + " не найден");
        }
        log.info("Удален фильм с id: {}", filmId);
        filmStorage.deleteFilm(filmId);
    }

    @Override
    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id);
        log.info("Запрошен фильм с id: {}. Найден фильм: {}", id, film);
        return film;
    }

    private Film validateFilmAndUser(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        if (film == null) {
            String errorMessage = "Фильм с id=" + filmId + " не существует.";
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        if (user == null) {
            String errorMessage = "Пользователь с id=" + filmId + " не существует.";
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
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
}
