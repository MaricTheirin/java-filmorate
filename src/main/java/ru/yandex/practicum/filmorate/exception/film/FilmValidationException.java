package ru.yandex.practicum.filmorate.exception.film;

import ru.yandex.practicum.filmorate.exception.ControllerException;

public class FilmValidationException extends ControllerException {

    public FilmValidationException(String message) {
        super(message);
    }

}
