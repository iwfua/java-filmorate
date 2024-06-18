package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film newFilm);

    Film deleteFilm(Film film);

    Film updateFilm(Film film);

    List<Film> findFilms();

    Film getFilmById(Long id);
}
