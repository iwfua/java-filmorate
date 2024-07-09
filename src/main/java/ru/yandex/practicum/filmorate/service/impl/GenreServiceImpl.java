package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.interfaces.GenreService;
import ru.yandex.practicum.filmorate.storage.db.GenreDBStorage;

import java.util.List;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    @Autowired
    private final GenreDBStorage genreDBStorage;

    @Override
    public Genre getGenreById(int id) {
        log.info("Запрошен жанр с id: {}", id);
        Genre genre = genreDBStorage.getGenreById(id);
        if (genre == null) {
            String errorMessage = "Жанр с id=" + id + " не найден";
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        log.info("Найден жанр: {}", genre);
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        log.info("Запрошен список всех жанров");
        List<Genre> genres = genreDBStorage.getAllGenres();
        log.info("Количество жанров: {}", genres.size());
        return genres;
    }
}
