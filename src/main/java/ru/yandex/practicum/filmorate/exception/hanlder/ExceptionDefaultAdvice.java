package ru.yandex.practicum.filmorate.exception.hanlder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ControllerException;

@Slf4j
@ControllerAdvice
public class ExceptionDefaultAdvice {

    @ExceptionHandler({
            ControllerException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<Response> handleException(Exception e) {
        String exceptionDescription = "Возникла ошибка при обработке запроса: ".concat(e.getMessage());
        Response response = new Response(exceptionDescription);
        HttpStatus responseStatus;

        if (e instanceof ControllerException) {
            responseStatus = ((ControllerException) e).getResponseStatus();
        } else {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        log.warn(exceptionDescription);
        return new ResponseEntity<>(response, responseStatus);
    }

}
