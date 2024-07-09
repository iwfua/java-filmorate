package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreById(int id);
}
