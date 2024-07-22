package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.interfaces.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        log.info("Пришел запрос GET /genres/{}", id);
        Genre genre = genreService.getGenreById(id);
        log.info("Отправлен ответ GET /genres/{} с телом: {}", id, genre);
        return genre;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Пришел запрос GET /genres");
        List<Genre> genres = genreService.getAllGenres();
        log.info("Отправлен ответ GET /genres с телом: {}", genres);
        return genres;
    }
}
