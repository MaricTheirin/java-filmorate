package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotImplementedException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("inMemoryGenreStorage")
public class InMemoryGenreStorage implements GenreStorage {

    Map<Integer,Genre> genres;
    private void initializeGenres() {
        genres = new HashMap<>();
        genres.put(1, new Genre(1, "Комедия"));
        genres.put(2, new Genre(2, "Драма"));
        genres.put(3, new Genre(3, "Мультфильм"));
        genres.put(4, new Genre(4, "Триллер"));
        genres.put(5, new Genre(5, "Документальный"));
        genres.put(6, new Genre(6, "Боевик"));
    }

    public InMemoryGenreStorage() {
        initializeGenres();
    }

    @Override
    public Genre get(Integer id) {
        return genres.get(id);
    }

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(genres.values());
    }

    @Override
    public boolean contains(Integer id) {
        return genres.containsKey(id);
    }

    @Override
    public Genre save(Genre genre) {
        throw new NotImplementedException("не реализовано");
    }

    @Override
    public Genre update(Genre genre) {
        throw new NotImplementedException("не реализовано");
    }

    @Override
    public Genre remove(Integer id) {
        throw new NotImplementedException("не реализовано");
    }
}
