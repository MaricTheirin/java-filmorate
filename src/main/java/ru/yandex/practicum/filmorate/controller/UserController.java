package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final Map<String, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.debug("Создание нового пользователя {}", user);
        if (users.containsKey(user.getLogin())) {
            log.warn("Невозможно создать пользователя {} из-за конфликта логинов.", user);
            throw new UserAlreadyExistsException("Пользователь с таким логином уже существует!");
        }
        return addUser(user);
    }

    @PutMapping
    public User createOrUpdateUser(@RequestBody User user) {
        log.debug("Создание/обновление пользователя {}", user);
        return addUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Получение списка всех пользователей");
        return new ArrayList<>(users.values());
    }

    private User addUser(User user) {
        users.put(user.getLogin(), user);
        log.info("Пользователь {} добавлен. Текущее количество пользователей: {}", user, users.size());
        return user;
    }

}


