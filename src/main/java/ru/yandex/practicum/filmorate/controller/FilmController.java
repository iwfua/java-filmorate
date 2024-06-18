package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films")
public class FilmController {
    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Пришел запрос GET /films");
        List<Film> films = filmStorage.findFilms();
        log.info("Отправлен ответ GET /films с телом: {}", films);
        return films;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("Пришел запрос POST /films с телом: {}", newFilm);
        Film createdFilm = filmStorage.createFilm(newFilm);
        log.info("Отправлен ответ POST /films: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Пришел запрос PUT /films с телом: {}", film);
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Отправлен ответ PUT /films: {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пришел запрос PUT /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
        log.info("Отправлен ответ PUT /films/{}/like/{}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пришел запрос DELETE /films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
        log.info("Отправлен ответ DELETE /films/{}/like/{}", id, userId);
    }

    @GetMapping("/popular")
    @ResponseBody
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Пришел запрос GET /films/popular?count={}", count);
        List<Film> popularFilms = filmService.getTopFilms(count);
        log.info("Отправлен ответ GET /films/popular?count={} с телом: {}", count, popularFilms);
        return popularFilms;
    }
}
