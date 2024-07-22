package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface RatingMPAService {
    RatingMpa getRatingMpaById(int id);

    List<RatingMpa> getRatingsMpa();
}
