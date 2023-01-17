package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("inMemoryFilmStorage")
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
        film.setId(++nextID);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film remove(Integer id) {
        return films.remove(id);
    }

    @Override
    public Film update(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public boolean contains(Integer id) {
        return films.containsKey(id);
    }
}
