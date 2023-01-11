package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends FilmorateController<Film> {

    protected Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Добавление нового фильма {}", film);
        if (film.getId() != 0) {
            throw new FilmValidationException("ID должен присваиваться автоматически");
        }
        film.setId(films.size() + 1);
        return save(film);
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        if (film.getId() == 0) {
            return create(film);
        }
        if (films.containsKey(film.getId())) {
            log.debug("Обновление существующего фильма {} на {}", films.get(film.getId()), film);
            return save(film);
        }
        throw new FilmNotFoundException("фильма с указанным ID не существует, обновление невозможно");
    }

    @Override
    public List<Film> getAll() {
        log.debug("Вывод всех добавленных фильмов");
        return new ArrayList<>(films.values());
    }

    private void validateFilm(Film film) {
        log.debug("Дополнительная валидация");
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Валидация неуспешна: дата создания фильма не должна быть раньше {}",MIN_RELEASE_DATE);
            throw new FilmValidationException(
                    "информация о фильме заполнена некорректно, дата создания не должна быть раньше " + MIN_RELEASE_DATE
            );
        }
        log.debug("Валидация успешна");
    }

    @Override
    protected Film save(Film film) {
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен. Текущее количество фильмов: {}", film, films.size());
        return films.get(film.getId());
    }

}