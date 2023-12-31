package kr.rogarithm.todos.domain.todo.controller;

import java.util.List;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.dto.UpdateTodoRequest;
import kr.rogarithm.todos.domain.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return ResponseEntity.status(HttpStatus.OK).body(todoResponse);
    }

    @GetMapping("")
    public ResponseEntity<List<TodoResponse>> getTodos(
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam Long size
    ) {
        List<TodoResponse> todos = todoService.getTodos(state, size);
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }

    @PostMapping("")
    public ResponseEntity<Void> addTodo(@RequestBody AddTodoRequest addTodoRequest) {

        todoService.saveTodo(addTodoRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("")
    public ResponseEntity<Void> updateTodo(@RequestBody UpdateTodoRequest updateTodoRequest) {

        todoService.updateTodo(updateTodoRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}