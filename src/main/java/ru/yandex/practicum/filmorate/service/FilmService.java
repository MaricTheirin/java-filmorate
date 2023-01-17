package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void like(Film film, User user) {
        Set<Integer> filmLikes = film.getUserLikes();

        if (!filmLikes.contains(user.getId())){
            filmLikes.add(user.getId());
            log.debug(
                    "Фильм {} получил лайк пользователя {}. Текущее количество лайков: {}",
                    film.getName(),
                    user.getLogin(),
                    filmLikes.size()
            );
        } else {
            log.debug("Лайк не добавлен - фильм {} уже имеет лайк пользователя {}", film.getName(), user.getLogin());
        }
    }

    public void dislike(Film film, User user) {
        Set<Integer> filmLikes = film.getUserLikes();

        if (filmLikes.contains(user.getId())) {
            filmLikes.remove(user.getId());
            log.debug("С фильма {} убран лайк пользователя {}. Текущее количество лайков: {}",
                    film.getName(),
                    user.getLogin(),
                    filmLikes.size()
            );
        } else {
            log.debug("Лайк не снят - фильм {} не имеет лайков от пользователя {}", film.getName(), user.getLogin());
        }
    }

    public List<Film> getTopFilms(Integer maxSize) {

        List<Film> topFilms =  filmStorage.getAll().stream()
                .sorted(getLikeComparator(true))
                .limit(maxSize)
                .collect(Collectors.toList());
        log.debug(
                "Запрошен список популярных фильмов. Результат: {}",
                topFilms.stream()
                        .map(film -> film.getName() + "[" + film.getUserLikes().size() + "]")
                        .collect(Collectors.joining(";"))
        );
        return topFilms;
    }

    private void validateFilm(Film film) {
        log.debug("Дополнительная валидация");
        if (film.getReleaseDate() == null) {
            log.warn("Фильм {} не обработан - не указана дата релиза", film);
            throw new UserValidationException("Не заполнено поле с датой создания фильма");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Валидация неуспешна: дата создания фильма не должна быть раньше {}",MIN_RELEASE_DATE);
            throw new FilmValidationException(
                    "информация о фильме заполнена некорректно, дата создания не должна быть раньше " + MIN_RELEASE_DATE
            );
        }
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }
        if (film.getUserLikes() == null) {
            film.setUserLikes(new HashSet<>());
        }
        log.debug("Валидация успешна");
    }

    public Film save(Film film) {
        log.debug("Запрошено сохранение фильма {}", film);
        validateFilm(film);
        Film savedFilm = filmStorage.save(film);
        log.debug("Фильм {} сохранён", savedFilm);
        return savedFilm;
    }

    public Film update(Film film) {
        log.debug("Запрос на обновление фильма");

        if (!filmStorage.contains(film.getId())) {
            throw new FilmNotFoundException("фильма с указанным ID не существует, обновление невозможно");
        }
        Film previousFilm = get(film.getId());
        validateFilm(film);
        Film updatedFilm = filmStorage.update(film);
        log.info("Фильм {} обновлён. Старое значение: {}. Новое значение: {}",
                updatedFilm.getName(), previousFilm, updatedFilm
        );
        return updatedFilm;
    }

    public List<Film> getAll() {
        log.debug("Запрошен список всех фильмов");
        List<Film> films =  filmStorage.getAll();
        log.trace("Получен список всех ({}) фильмов: {}", films.size(), films);
        return films;
    }

    public Film get(Integer id) {
        log.debug("Запрошен фильм с id = {}", id);
        if (isExists(id)) {
            Film requestedFilm = filmStorage.get(id);
            log.trace("Полученный фильм: {}", requestedFilm);
            return requestedFilm;
        } else {
            log.warn("Запрошеный фильм с id = {} не существует", id);
            throw new FilmNotFoundException("Не найден фильм с id = " + id);
        }
    }

    public Film remove(Integer id) {
        log.debug("Запрошено удаление фильма с id = {}", id);
        if (isExists(id)) {
            Film film = filmStorage.remove(id);
            log.info("Фильм {} удалён", film);
            return film;
        }
        log.warn("Запрошеный фильм с id = {} не существует", id);
        throw new FilmNotFoundException("Не найден фильм с id = " + id);
    }

    private boolean isExists(Integer id) {
        boolean result = filmStorage.contains(id);
        log.trace("Проверка существования фильма с id = {}, результат = {}", id, result);
        return result;
    }

    private Comparator<Film> getLikeComparator(boolean reverseOrder) {
        int multiplier = reverseOrder ? -1 : 1;

        return (f1, f2) -> {
            int likes1 = f1.getUserLikes().size();
            int likes2 = f2.getUserLikes().size();
            if (likes1 < likes2) {
                return -1 * multiplier;
            } else if (f1 == f2) {
                return 0;
            } else {
                return multiplier;
            }
        };
    }

}
