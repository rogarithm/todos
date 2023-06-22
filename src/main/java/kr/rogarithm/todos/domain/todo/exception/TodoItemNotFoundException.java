package kr.rogarithm.todos.domain.todo.exception;

public class TodoItemNotFoundException extends RuntimeException {

    public TodoItemNotFoundException(String message) {
        super(message);
    }
}