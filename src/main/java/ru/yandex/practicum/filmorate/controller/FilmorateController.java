package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

public abstract class FilmorateController <T> {

    public abstract T create(@Valid @RequestBody T t);

    public abstract T update(@Valid @RequestBody T t);

    public abstract T getById(Integer id);

    public abstract List<T> getAll();

}
