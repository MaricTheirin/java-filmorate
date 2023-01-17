package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRatingStorage {

    List<Mpa> getAll();

    Mpa getById(Integer id);

    boolean contains(Integer id);

}
