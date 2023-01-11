package ru.yandex.practicum.filmorate.exception.user;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ControllerException;

public class UserNotFoundException extends ControllerException {
    public UserNotFoundException(String message) {
        super(message);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
