package ru.yandex.practicum.filmorate.exception.film;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ControllerException;

public class FilmNotFoundException extends ControllerException {

    public FilmNotFoundException(String message) {
        super(message);
        responseStatus = HttpStatus.NOT_FOUND;
    }

}
