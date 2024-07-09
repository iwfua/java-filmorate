package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.interfaces.RatingMPAService;
import ru.yandex.practicum.filmorate.storage.db.RatingMPAStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingMPAServiceImpl implements RatingMPAService {
    @Autowired
    private final RatingMPAStorage ratingMPAStorage;

    @Override
    public RatingMpa getRatingMpaById(int id) {
        log.info("Запрошен рейтинг MPA с id: {}", id);
        RatingMpa mpa = ratingMPAStorage.getRatingMpaById(id);
        if (mpa == null) {
            String errorMessage = "Рейтинг с id=" + id + " не найден";
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        log.info("Найден рейтинг: {}", mpa);
        return mpa;
    }

    @Override
    public List<RatingMpa> getRatingsMpa() {
        log.info("Запрошен список всех рейтингов MPA");
        List<RatingMpa> ratings = ratingMPAStorage.getRatingsMpa();
        log.info("Количество рейтингов MPA: {}", ratings.size());
        return ratings;
    }
}
