package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    protected Map<Integer, Film> films = new HashMap<>();
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private int nextID = 0;

    @Autowired
    public InMemoryFilmStorage(
            @Qualifier("inMemoryMpaStorage") MpaStorage mpaStorage,
            @Qualifier("inMemoryGenreStorage")GenreStorage genreStorage
    ) {
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

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
        updateFilmFields(film);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    private void updateFilmFields(Film film) {
        film.setMpa(mpaStorage.get(film.getMpa().getId()));
        film.setGenres(
                film.getGenres().stream()
                        .map(filmGenre -> genreStorage.get(filmGenre.getId()))
                        .distinct()
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Film remove(Integer id) {
        return films.remove(id);
    }

    @Override
    public Film update(Film film) {
        updateFilmFields(film);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public boolean contains(Integer id) {
        return films.containsKey(id);
    }
}
