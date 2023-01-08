package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.user.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends FilmorateController<User> {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(@Valid @RequestBody User user) {
        log.debug("Добавление нового пользователя {}", user);
        if (user.getId() != 0) {
            throw new UserValidationException("Ошибка: ID должен присваиваться автоматически");
        }
        user.setId(users.size() + 1);
        return save(user);
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == 0) {
            return create(user);
        }
        if (users.containsKey(user.getId())) {
            log.debug("Обновление существующего пользователя {} на {}", users.get(user.getId()), user);
            return save(user);
        }
        throw new UserValidationException("Ошибка: ID должен присваиваться автоматически");
    }

    @Override
    public List<User> getAll() {
        log.debug("Вывод всех добавленных пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    protected User save(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен. Текущее количество пользователей: {}", user, users.size());
        return users.get(user.getId());
    }

}


