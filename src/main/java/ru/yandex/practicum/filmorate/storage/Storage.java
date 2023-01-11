package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage <T> {

    T get(Integer id);

    List<T> getAll();

    T save(T t);

    T remove(Integer id);

    T update (T t);

    int getNextID();

    boolean contains(Integer id);

}
