package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotImplementedException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("inMemoryMpaStorage")
public class InMemoryMpaStorage implements MpaStorage {

    Map<Integer, Mpa> mpaRatings;
    private void initializeMpaRatings() {
        mpaRatings = new HashMap<>();
        mpaRatings.put(1, new Mpa(1, "G"));
        mpaRatings.put(2, new Mpa(2, "PG"));
        mpaRatings.put(3, new Mpa(3, "PG-13"));
        mpaRatings.put(4, new Mpa(4, "R"));
        mpaRatings.put(5, new Mpa(5, "NC-17"));
    }

    public InMemoryMpaStorage() {
        initializeMpaRatings();
    }

    @Override
    public Mpa get(Integer id) {
        return mpaRatings.get(id);
    }

    @Override
    public List<Mpa> getAll() {
        return new ArrayList<>(mpaRatings.values());
    }

    @Override
    public boolean contains(Integer id) {
        return mpaRatings.containsKey(id);
    }

    @Override
    public Mpa save(Mpa mpa) {
        throw new NotImplementedException("не реализовано");
    }

    @Override
    public Mpa update(Mpa mpa) {
        throw new NotImplementedException("не реализовано");
    }

    @Override
    public Mpa remove(Integer id) {
        throw new NotImplementedException("не реализовано");
    }

}
