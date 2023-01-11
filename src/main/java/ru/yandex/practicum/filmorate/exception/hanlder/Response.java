package ru.yandex.practicum.filmorate.exception.hanlder;

import lombok.Value;

import java.time.Instant;

@Value
public class Response {

    Instant timestamp = Instant.now();
    String message;

}
