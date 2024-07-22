package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends LikesStorage {
    Film createFilm(Film newFilm);

    String deleteFilm(int filmId);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(int id);
}
