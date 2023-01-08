package ru.yandex.practicum.filmorate.exception.user;

import ru.yandex.practicum.filmorate.exception.ControllerException;

public class UserValidationException extends ControllerException {
    public UserValidationException(String message) {
        super(message);
    }
}
