package ru.yandex.practicum.filmorate.exception.film;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ControllerException;

public class FilmAlreadyExistsException extends ControllerException {

    public FilmAlreadyExistsException(String message) {
        super(message);
        responseStatus = HttpStatus.BAD_REQUEST;
    }
}
