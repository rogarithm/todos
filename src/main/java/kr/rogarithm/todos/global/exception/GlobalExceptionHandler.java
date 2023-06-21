package kr.rogarithm.todos.global.exception;

import kr.rogarithm.todos.todo.exception.TodoItemNotFoundException;
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

}