package ru.yandex.practicum.filmorate.exception.hanlder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.mpaRating.MpaRatingNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;

import java.util.StringJoiner;

@Slf4j
@RestControllerAdvice
public class ExceptionDefaultAdvice {

    private static final String DEFAULT_EXCEPTION_DESCRIPTION = "Возникла ошибка при обработке запроса: ";

    @ExceptionHandler({
            UserNotFoundException.class,
            FilmNotFoundException.class,
            GenreNotFoundException.class,
            MpaRatingNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleNotFoundExceptions (Throwable e) {
        return new Response(DEFAULT_EXCEPTION_DESCRIPTION + e.getMessage());
    }

    @ExceptionHandler({FilmValidationException.class, UserValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleValidationExceptions (Throwable e) {
        return new Response(DEFAULT_EXCEPTION_DESCRIPTION + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleArgumentValidationExceptions (Throwable e) {
        StringJoiner sj = new StringJoiner("; ");
        for (FieldError error: ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()) {
            sj.add("Поле [" + error.getField() + "] " + error.getDefaultMessage());
        }
        return new Response(DEFAULT_EXCEPTION_DESCRIPTION + sj);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleHttpMessageNotReadableExceptions (Throwable e) {
        return new Response(DEFAULT_EXCEPTION_DESCRIPTION + ((HttpMessageNotReadableException) e).getRootCause());
    }

    @ExceptionHandler (Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleOtherExceptions(Throwable e) {
        return new Response(DEFAULT_EXCEPTION_DESCRIPTION + e.getMessage());
    }

}
