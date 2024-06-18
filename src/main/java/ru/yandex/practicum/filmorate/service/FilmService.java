package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getTopFilms(int count);

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);
}
