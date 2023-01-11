package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends FilmorateController<Film> {

    protected FilmStorage filmStorage;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Добавление нового фильма {}", film);
        if (film.getId() != 0) {
            throw new FilmValidationException("ID должен присваиваться автоматически");
        }
        film.setId(filmStorage.getNextID());
        validateFilm(film);
        return filmStorage.save(film);
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        if (film.getId() == 0) {
            return create(film);
        }
        if (filmStorage.contains(film.getId())) {
            log.debug("Обновление существующего фильма {} на {}", filmStorage.get(film.getId()), film);
            return filmStorage.save(film);
        }
        throw new FilmNotFoundException("фильма с указанным ID не существует, обновление невозможно");
    }

    @Override
    public List<Film> getAll() {
        log.debug("Вывод всех добавленных фильмов");
        return filmStorage.getAll();
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

}
