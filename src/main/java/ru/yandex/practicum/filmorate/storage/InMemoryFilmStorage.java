package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    protected Map<Integer, Film> films = new HashMap<>();
    private int nextID = 0;

    @Override
    public Film get(Integer id) {
        log.debug("Запрошен фильм с id = {}", id);
        Film film = films.get(id);
        log.trace("Полученный фильм: {}", film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> allFilms = new ArrayList<>(films.values());
        log.trace("Запрошен список фильмов: {}", allFilms);
        return allFilms;
    }

    @Override
    public Film save(Film film) {
        film.setId(++nextID);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен. Текущее количество фильмов: {}", film, films.size());
        return films.get(film.getId());
    }

    @Override
    public Film remove(Integer id) {
        Film film = films.remove(id);
        log.info("Фильм {} удалён", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Film previousValue = films.put(film.getId(), film);
        log.info("Фильм {} обновлён. Старое значение: {}. Новое значение: {}", film.getName(), previousValue, film);
        return film;
    }

    @Override
    public boolean contains(Integer id) {
        boolean isExist = films.containsKey(id);
        log.trace("Проверка существования фильма с id = {}, результат = {}", id, isExist);
        return isExist;
    }
}
