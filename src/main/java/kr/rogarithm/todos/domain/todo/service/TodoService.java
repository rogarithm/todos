package kr.rogarithm.todos.domain.todo.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import kr.rogarithm.todos.domain.todo.dao.TodoMapper;
import kr.rogarithm.todos.domain.todo.domain.Todo;
import kr.rogarithm.todos.domain.todo.dto.AddTodoRequest;
import kr.rogarithm.todos.domain.todo.dto.TodoResponse;
import kr.rogarithm.todos.domain.todo.exception.TodoItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class TodoService {

    private final TodoMapper todoMapper;

    public TodoService(TodoMapper todoMapper) {

        this.todoMapper = todoMapper;
    }

    public TodoResponse getTodoById(Long todoId) {

        Todo todo = todoMapper.selectTodoById(todoId);

        if (todo == null) {
            throw new TodoItemNotFoundException("입력한 아이디 " + todoId + "에 해당하는 할 일 항목 정보를 찾을 수 없습니다");
        }

        return TodoResponse.of(todo);
    }

    public List<TodoResponse> getTodos(String state, Long size) {

        if (state.equals("INCOMPLETE")) {
            List<Todo> incompletes = todoMapper.selectTodos("INCOMPLETE", size);
            return incompletes.stream()
                              .map(TodoResponse::of)
                              .collect(Collectors.toList());
        }
        if (state.equals("COMPLETE")) {
            List<Todo> completes = todoMapper.selectTodos("COMPLETE", size);
            return completes.stream()
                            .map(TodoResponse::of)
                            .collect(Collectors.toList());
        } else {
            List<Todo> incompletes = todoMapper.selectTodos("INCOMPLETE", size);
            List<Todo> completes = todoMapper.selectTodos("COMPLETE", size);
            return Stream.concat(incompletes.stream(), completes.stream())
                         .map(TodoResponse::of)
                         .collect(Collectors.toList());
        }
    }

    public void saveTodo(@Valid AddTodoRequest addTodoRequest) {

        todoMapper.insertTodo(addTodoRequest.toTodo());
    }
}