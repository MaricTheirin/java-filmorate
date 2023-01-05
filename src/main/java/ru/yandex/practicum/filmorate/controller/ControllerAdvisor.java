package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ControllerException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<Object> handleControllerException(ControllerException e, WebRequest request) {
        return new ResponseEntity<>(generateResponse(request, e), e.getResponseStatus());
    }

    private static Map<String, Object> generateResponse(WebRequest request, ControllerException e) {
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("timestamp", e.getTimestamp());
        responseBody.put("status", e.getResponseStatus());
        responseBody.put("error", e.getMessage());
        responseBody.put("path", ((ServletWebRequest)request).getRequest().getRequestURI());
        return responseBody;
    }

}
