package ru.yandex.practicum.filmorate.exception.mpaRating;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ControllerException;

public class MpaRatingNotFoundException extends ControllerException {
    public MpaRatingNotFoundException(String message) {
        super(message);
        responseStatus = HttpStatus.NOT_FOUND;
    }
}
