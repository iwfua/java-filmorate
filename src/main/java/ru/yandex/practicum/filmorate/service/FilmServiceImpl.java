package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static final LocalDate DATE_RELEASE = LocalDate.of(1895, 12, 28);
    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    private final UserStorage userStorage;


    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = validateFilmAndUser(filmId, userId);
        film.addLike(userId);
        log.info("Лайк добавлен фильму с id: {} от пользователя с id: {}", filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        Film film = validateFilmAndUser(filmId, userId);
        film.deleteLike(userId);
        log.info("Лайк удален фильму с id: {} от пользователя с id: {}", filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::sizeOfLikes).reversed())
                .limit(count)
                .toList();
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film createFilm(Film film) {
        validateDateRelease(film);
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        validateId(film);
        validateDateRelease(film);
        return filmStorage.updateFilm(film);
    }

    private Film validateFilmAndUser(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        if (film == null) {
            String errorMessage = "Фильма с id=" + filmId + " не существует.";
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        if (user == null) {
            String errorMessage = "Пользователя с id=" + filmId + " не существует.";
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
