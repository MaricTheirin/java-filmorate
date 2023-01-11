package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends FilmorateController<Film> {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Добавление нового фильма {}", film);
        if (film.getId() != 0) {
            throw new FilmValidationException("ID должен присваиваться автоматически");
        }
        return filmService.create(film);
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        if (film.getId() == 0) {
            return create(film);
        }
        return filmService.update(film);
    }

    @Override
    public List<Film> getAll() {
        log.debug("Вывод всех добавленных фильмов");
        return filmService.getAll();
    }

}
