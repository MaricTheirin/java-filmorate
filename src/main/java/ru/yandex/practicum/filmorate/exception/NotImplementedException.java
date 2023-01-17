package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class NotImplementedException extends ControllerException{

    public NotImplementedException(String message) {
        super(message);
        responseStatus = HttpStatus.NOT_IMPLEMENTED;
    }
}
