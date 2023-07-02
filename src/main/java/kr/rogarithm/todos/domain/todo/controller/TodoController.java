package kr.rogarithm.todos.domain.todo.controller;

import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable(name = "todoId") Long todoId) {
        TodoResponse todoResponse = todoService.getTodoById(todoId);
        return ResponseEntity.ok(todoResponse);
    }

    @PostMapping("")
    public ResponseEntity<Void> addTodo(@RequestBody AddTodoRequest addTodoRequest) {
        todoService.saveTodo(addTodoRequest);
        return ResponseEntity.ok().build();
    }
}