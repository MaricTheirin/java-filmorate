package ru.yandex.practicum.filmorate.exception.user;

import ru.yandex.practicum.filmorate.exception.ControllerException;

public class UserAlreadyExistsException extends ControllerException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
