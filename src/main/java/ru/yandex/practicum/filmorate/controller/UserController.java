package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends FilmorateController<User> {

    UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User create(@Valid @RequestBody User user) {
        log.debug("Добавление нового пользователя {}", user);
        if (user.getId() != 0) {
            throw new UserValidationException("ID должен присваиваться автоматически");
        }
        user.setId(userStorage.getNextID());
        return userStorage.save(user);
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == 0) {
            return userStorage.save(user);
        }
        if (userStorage.contains(user.getId())) {
            log.debug("Обновление существующего пользователя {} на {}", userStorage.get(user.getId()), user);
            return userStorage.save(user);
        }
        throw new UserNotFoundException("пользователя с указанным ID не существует, обновление невозможно");
    }

    @Override
    public List<User> getAll() {
        log.debug("Вывод всех добавленных пользователей");
        return userStorage.getAll();
    }

}


