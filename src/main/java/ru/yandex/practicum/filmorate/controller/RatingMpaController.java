package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.interfaces.RatingMPAService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class RatingMpaController {
    private final RatingMPAService ratingMpaService;

    @Autowired
    public RatingMpaController(RatingMPAService ratingMpaService) {
        this.ratingMpaService = ratingMpaService;
    }

    @GetMapping
    public List<RatingMpa> getRatingsMpa() {
        log.info("Пришел запрос GET /mpa");
        List<RatingMpa> ratings = ratingMpaService.getRatingsMpa();
        log.info("Отправлен ответ GET /mpa с телом: {}", ratings);
        return ratings;
    }

    @GetMapping("/{id}")
    public RatingMpa getRatingMpaById(@PathVariable Integer id) {
        log.info("Пришел запрос GET /mpa с id: {}", id);
        RatingMpa rating = ratingMpaService.getRatingMpaById(id);
        if (rating == null) {
            log.warn("Рейтинг MPA с id {} не найден", id);
            throw new NotFoundException("Рейтинг MPA с id " + id + " не найден");
        }
        log.info("Отправлен ответ GET /mpa: {}", rating);
        return rating;
    }
}
