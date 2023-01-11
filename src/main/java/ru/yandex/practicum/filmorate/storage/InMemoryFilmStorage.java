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
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film save(Film film) {
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен. Текущее количество фильмов: {}", film, films.size());
        return films.get(film.getId());
    }

    @Override
    public Film remove(Integer id) {
        return films.remove(id);
    }

    @Override
    public Film update(Film film) {
        return save(film);
    }

    @Override
    public int getNextID() {
        return nextID++;
    }

    @Override
    public boolean contains(Integer id) {
        return films.containsKey(id);
    }
}
