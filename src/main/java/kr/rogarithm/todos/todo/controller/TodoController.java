package kr.rogarithm.todos.todo.controller;

import kr.rogarithm.todos.todo.dto.TodoResponse;
import kr.rogarithm.todos.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable Long todoId) {
        TodoResponse todo = todoService.getTodoById(todoId);
        return ResponseEntity.ok(todo);
    }
}