package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({HttpMessageConversionException.class, ValidationException.class})
    public ResponseEntity<Map<String, String>> handleValidation(final RuntimeException e) {
        return new ResponseEntity<>(
                Map.of("Ошибка в валидации", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidation(final MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                Map.of("Ошибка в валидации", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotFound(final NotFoundException e) {
        return new ResponseEntity<>(
                Map.of("Объект не найден", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleInternalServerError(final Exception e) {
        return new ResponseEntity<>(
                Map.of("Серверу не удается обработать запрос", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
