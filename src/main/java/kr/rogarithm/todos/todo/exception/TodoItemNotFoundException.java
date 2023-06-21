package kr.rogarithm.todos.todo.exception;

public class TodoItemNotFoundException extends RuntimeException {
    public TodoItemNotFoundException(String message) {
        super(message);
    }
}