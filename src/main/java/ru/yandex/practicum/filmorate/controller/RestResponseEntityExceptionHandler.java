package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
        log.warn("Произошла ошибка валидации: {}", ex.getBindingResult().getAllErrors());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
