package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends FilmorateController<Film> {

    FilmService filmService;
    UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @Override
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Добавление нового фильма {}", film);
        if (film.getId() != 0) {
            throw new FilmValidationException("ID должен присваиваться автоматически");
        }
        return filmService.save(film);
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (film.getId() == 0) {
            return create(film);
        }
        return filmService.update(film);
    }

    @PutMapping("{id}/like/{userId}")
    public void like(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.like(id, userId);
    }

    @Override
    @GetMapping
    public List<Film> getAll() {
        log.debug("Вывод всех добавленных фильмов");
        return filmService.getAll();
    }

    @Override
    @GetMapping("{id}")
    public Film getById(@PathVariable Integer id) {
        return filmService.get(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void dislike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.dislike(id, userId);
    }

    @DeleteMapping("{id}")
    public Film delete(@PathVariable Integer id) {
        return filmService.remove(id);
    }

}
