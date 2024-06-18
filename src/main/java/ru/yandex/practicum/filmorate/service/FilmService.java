package ru.yandex.practicum.filmorate.service;

import org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void addLike(Long filmId, Long userId);
    void deleteLike(Long filmId, Long userId);
    List<Film> getTopFilms(int count);

}
