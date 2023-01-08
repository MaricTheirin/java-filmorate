package ru.yandex.practicum.filmorate.exception.user;

import ru.yandex.practicum.filmorate.exception.ControllerException;

public class UserNotFoundException extends ControllerException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
