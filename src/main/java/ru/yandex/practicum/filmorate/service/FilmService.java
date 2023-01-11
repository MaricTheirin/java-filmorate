package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private static final int DEFAULT_TOP_LIMIT = 10;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void like(Film film, User user) {
        Set<Integer> filmLikes = film.getUserLikes();
        if (filmLikes.contains(user.getId())) {
            filmLikes.remove(user.getId());
            log.debug("Пользователь {} убрал лайк с фильма {}", user.getLogin(), film.getName());
        } else {
            filmLikes.add(user.getId());
            log.debug("Пользователь {} поставил лайк фильму {}", user.getLogin(), film.getName());
        }
    }

    public Map<Film, Integer> getTopFilms(Optional<Integer> limit) {

        return filmStorage.getAll().stream()
                .collect(Collectors.toMap(film -> film, film -> film.getUserLikes().size()));
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

    public Film create(Film film) {
        film.setId(filmStorage.getNextID());
        validateFilm(film);
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        if (filmStorage.contains(film.getId())) {
            log.debug("Обновление существующего фильма {} на {}", filmStorage.get(film.getId()), film);
            return filmStorage.save(film);
        }
        throw new FilmNotFoundException("фильма с указанным ID не существует, обновление невозможно");
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }
}
