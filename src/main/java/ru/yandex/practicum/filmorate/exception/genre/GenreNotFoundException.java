package ru.yandex.practicum.filmorate.exception.genre;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ControllerException;

public class GenreNotFoundException extends ControllerException {

    public GenreNotFoundException(String message) {
        super(message);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
