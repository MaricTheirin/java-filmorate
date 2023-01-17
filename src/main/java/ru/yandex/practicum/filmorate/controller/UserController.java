package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends FilmorateController<User> {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Добавление нового пользователя {}", user);
        if (user.getId() != 0) {
            throw new UserValidationException("ID должен присваиваться автоматически");
        }
        return userService.save(user);
    }

    @Override
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == 0) {
            return userService.save(user);
        }
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addToFriends(id, friendId);
    }

    @Override
    @GetMapping
    public List<User> getAll() {
        log.debug("Вывод всех добавленных пользователей");
        return userService.getAll();
    }

    @Override
    @GetMapping("{id}")
    public User getById(@PathVariable Integer id) {
        return userService.get(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonFriends(userService.get(id), userService.get(otherId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFromFriends(id, friendId);
    }

}


