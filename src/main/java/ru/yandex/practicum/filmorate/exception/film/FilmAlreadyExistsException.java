package ru.yandex.practicum.filmorate.exception.film;

import ru.yandex.practicum.filmorate.exception.ControllerException;

public class FilmAlreadyExistsException extends ControllerException {

    public FilmAlreadyExistsException(String message) {
        super(message);
    }
}
