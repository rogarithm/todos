package kr.rogarithm.todos.global.exception;

import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
import kr.rogarithm.todos.domain.user.exception.DuplicateAccountException;
import kr.rogarithm.todos.domain.user.exception.DuplicateNicknameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoItemNotFoundException.class)
    protected ResponseEntity<Void> todoItemNotFoundException(TodoItemNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(DuplicateAccountException.class)
    protected ResponseEntity<Void> duplicateAccountException(DuplicateAccountException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    protected ResponseEntity<Void> duplicateNicknameException(DuplicateNicknameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}