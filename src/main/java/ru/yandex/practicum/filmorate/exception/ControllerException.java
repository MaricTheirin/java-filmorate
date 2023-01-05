package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public abstract class ControllerException extends RuntimeException {

    private final HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
    private final Instant timestamp = Instant.now();

    public ControllerException(String message) {
        super(message);
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

}
