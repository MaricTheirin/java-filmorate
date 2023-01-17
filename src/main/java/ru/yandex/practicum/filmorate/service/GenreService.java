package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {

    GenreStorage genreStorage;

    @Autowired
    public GenreService(@Qualifier("dbGenreStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAll() {
        List<Genre> genres =  genreStorage.getAll();
        log.debug("Запрошено получение всех жанров, получено {} значений", genres.size());
        return genres;
    }

    public Genre getById(Integer id) {
        log.debug("Запрошено получение жанра с id = {}", id);

        if (!genreStorage.contains(id)) {
            String errMessage = "Жанра с id = " + id + " не существует";
            log.warn(errMessage);
            throw new GenreNotFoundException(errMessage);
        }
        Genre genre = genreStorage.getById(id);
        log.debug("Получено значение {}", genre);
        return genre;
    }

}
