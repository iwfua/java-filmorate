package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films")
public class FilmController {
    @Autowired
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Пришел запрос GET /films");
        List<Film> films = filmService.getAllFilms();
        log.info("Отправлен ответ GET /films с телом: {}", films);
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Пришел запрос GET /films/{}", id);
        Film film = filmService.getFilmById(id);
        log.info("Отправлен ответ GET /films/{} с телом: {}", id, film);
        return film;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("Пришел запрос POST /films с телом: {}", newFilm);
        Film createdFilm = filmService.createFilm(newFilm);
        log.info("Отправлен ответ POST /films: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Пришел запрос PUT /films с телом: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Отправлен ответ PUT /films: {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пришел запрос PUT /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
        log.info("Отправлен ответ PUT /films/{}/like/{}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пришел запрос DELETE /films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
        log.info("Отправлен ответ DELETE /films/{}/like/{}", id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Пришел запрос GET /films/popular?count={}", count);
        List<Film> popularFilms = filmService.getTopFilms(count);
        log.info("Отправлен ответ GET /films/popular?count={} с телом: {}", count, popularFilms);
        return popularFilms;
    }
}
