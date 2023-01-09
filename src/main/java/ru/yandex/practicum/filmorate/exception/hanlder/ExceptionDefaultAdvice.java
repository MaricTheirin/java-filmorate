package ru.yandex.practicum.filmorate.exception.hanlder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ControllerException;
import java.util.StringJoiner;

@Slf4j
@ControllerAdvice
public class ExceptionDefaultAdvice {

    private static final String DEFAULT_EXCEPTION_DESCRIPTION = "Возникла ошибка при обработке запроса: ";

    @ExceptionHandler({
            ControllerException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<Response> handleException(Exception e) {
        String exceptionDescription;
        HttpStatus responseStatus;

        if (e instanceof HttpMessageNotReadableException) {
            exceptionDescription = DEFAULT_EXCEPTION_DESCRIPTION + ((HttpMessageNotReadableException) e).getRootCause();
            responseStatus =  HttpStatus.BAD_REQUEST;
        } else if (e instanceof ControllerException) {
            exceptionDescription = DEFAULT_EXCEPTION_DESCRIPTION + e.getMessage();
            responseStatus = ((ControllerException) e).getResponseStatus();
        } else if (e instanceof MethodArgumentNotValidException) {
            StringJoiner sj = new StringJoiner("; ");
            for (FieldError error: ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()) {
                sj.add("Поле [" + error.getField() + "] " + error.getDefaultMessage());
            }
            exceptionDescription = DEFAULT_EXCEPTION_DESCRIPTION + sj;
            responseStatus = HttpStatus.BAD_REQUEST;
        } else {
            exceptionDescription = DEFAULT_EXCEPTION_DESCRIPTION + e.getMessage();
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        log.warn(exceptionDescription);
        return new ResponseEntity<>(new Response(exceptionDescription), responseStatus);
    }

}
