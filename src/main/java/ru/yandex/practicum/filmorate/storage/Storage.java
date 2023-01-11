package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage <T> {

    T get(Integer id);

    T save(T t);

    T update (T t);

    T remove(Integer id);

    List<T> getAll();

    boolean contains(Integer id);

}
