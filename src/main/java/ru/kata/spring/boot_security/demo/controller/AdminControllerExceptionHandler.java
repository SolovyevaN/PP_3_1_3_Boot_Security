package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kata.spring.boot_security.demo.exception.DuplicatePasswordException;
import ru.kata.spring.boot_security.demo.exception.UserNotFindExeption;

@RestControllerAdvice
public class AdminControllerExceptionHandler {

    // Обработка ошибки дублирующегося пароля
    @ExceptionHandler(ru.kata.spring.boot_security.demo.exception.DuplicatePasswordException.class)
    public ResponseEntity<String> handleDuplicatePassword(DuplicatePasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Обработка ошибки "пользователь не найден"
    @ExceptionHandler(UserNotFindExeption.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFindExeption ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
