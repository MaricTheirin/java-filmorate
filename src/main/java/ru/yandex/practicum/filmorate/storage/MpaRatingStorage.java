package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingStorage {

    List<MpaRating> getAll();

    MpaRating getById(Integer id);

    boolean contains(Integer id);

}
