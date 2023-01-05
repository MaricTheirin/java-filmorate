package ru.yandex.practicum.filmorate.exception;

public class FilmAlreadyExistsException extends ControllerException {

    public FilmAlreadyExistsException(String message) {
        super(message);
    }
}
