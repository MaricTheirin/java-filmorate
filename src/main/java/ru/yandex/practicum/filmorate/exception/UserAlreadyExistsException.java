package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistsException extends ControllerException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
