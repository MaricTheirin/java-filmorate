package ru.yandex.practicum.filmorate.exception.user;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ControllerException;

public class UserValidationException extends ControllerException {
    public UserValidationException(String message) {
        super(message);
        responseStatus = HttpStatus.BAD_REQUEST;
    }
}
