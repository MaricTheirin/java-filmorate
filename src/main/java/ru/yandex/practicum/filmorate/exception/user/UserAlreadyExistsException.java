package ru.yandex.practicum.filmorate.exception.user;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ControllerException;

public class UserAlreadyExistsException extends ControllerException {

    public UserAlreadyExistsException(String message) {
        super(message);
        responseStatus = HttpStatus.BAD_REQUEST;
    }
}
