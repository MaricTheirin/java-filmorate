package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/film")
public class FilmController {

    Map<String, Film> films = new HashMap<>();
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.debug("Создание нового фильма {}", film);
        String filmIdentification = film.getName()+film.getReleaseDate();
        if (films.containsKey(filmIdentification)) {
            log.warn("Невозможно создать фильм {}, пара 'наименование' и 'дата релиза' не уникальна", film);
            throw new FilmAlreadyExistsException("Фильм с таким наименованием и датой выхода уже существует");
        }
        return addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Создание/обновление фильма {}", film);
        return addFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Получение списка всех фильмов");
        return new ArrayList<>(films.values());
    }

    private boolean validateFilm(Film film) {

        log.debug("Валидация фильма {}", film);
        if (film.getName().length() == 0) {
            log.warn("Валидация неуспешна: Наименование фильма не может быть пустым");
            return false;
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Валидация неуспешна: Длина фильма не должна превышать {} символов", MAX_DESCRIPTION_LENGTH);
            return false;
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Валидация неуспешна: Дата создания фильма не должна быть раньше {}",MIN_RELEASE_DATE);
            return false;
        }
        if (film.getDuration() <= 0) {
            log.warn("Валидация неуспешна: Длительность фильма должна быть положительной");
            return false;
        }
        log.debug("Валидация прошла успешно");
        return true;
    }

    private Film addFilm(Film film) {
        String filmIdentification = film.getName()+film.getReleaseDate();

        if (!validateFilm(film)) {
            throw new FilmValidationException("Информация о фильме не заполнена некорректно");
        }

        if (films.containsKey(filmIdentification)) {
            film.setId(films.get(filmIdentification).getId());
        } else {
            film.setId(films.size());
        }
        films.put(filmIdentification, film);
        log.info("Фильм {} добавлен. Текущее количество фильмов: {}", film, films.size());
        return films.get(filmIdentification);
    }

}
