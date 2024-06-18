package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
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
        return filmStorage.findFilms().stream()
                .sorted(Comparator.comparingInt(Film::sizeOfLikes).reversed())
                .limit(count)
                .toList();
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
}
