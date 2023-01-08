package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

public abstract class FilmorateController <T> {

    @PostMapping
    public abstract T create(@Valid @RequestBody T t);

    @PutMapping
    public abstract T update(@Valid @RequestBody T t);

    @GetMapping
    public abstract List<T> getAll();

    protected abstract T save(T t);

}
